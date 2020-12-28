package com.edu.member.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.common.service.NotiService;
import com.edu.member.domain.MemberDTO;
import com.edu.member.domain.PointDTO;
import com.edu.member.domain.RewardDTO;
import com.edu.member.service.MemberService;
import com.edu.member.service.PointService;
import com.edu.member.service.RewardService;

@Controller // 컨트롤러 빈으로 등록하는 어노테이션
@RequestMapping("/point/*") // PointController에서 공통적으로 사용될 url mapping
public class PointController {
	
	// 로깅을 위한 변수 logger를 선언한다.
	private static final Logger logger = LoggerFactory.getLogger(PointController.class);
	
	@Inject
	PointService pointService;
	
	@Inject
	MemberService memberService;
	
	@Inject
	NotiService notiService;
	
	@Inject
	RewardService rewardService;
	
	// 포인트 지급
	// ajax로 호출할 때 return 값이 있는데 @ResponseBody 붙이지 않았을 때
	// java.lang.IllegalArgumentException: Unknown return value type: java.lang.Integer 오류 발생
	// @ResponseBody 붙이거나 반환타입을 void로 바꾸면 해결 됨
	@ResponseBody
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	private int addNewPoint(HttpSession session, String pointContent, String member, int changeVal) throws Exception {
		logger.info("PointController addNewPoint()....");
		// 포인트 객체 만들어 전달 받은 내용 입력
		PointDTO pointDTO = new PointDTO();
		pointDTO.setContent(pointContent);
		pointDTO.setMember(member);
		pointDTO.setChangeVal(changeVal);
		logger.info("PointController addNewPoint() pointDTO: " + pointDTO);
		// 중복된 포인트 지급 내역이 있는지 확인하기 위해 내용을 담아 서비스에 의뢰한다.
		// 같은 내용의 포인트 지급 내역이 있으면 포인트를 지급하지 않는다.
		int numOfSearchPointContent = pointService.getNumOfSearchPointContent(pointDTO);
		// TIL이나 출석 포인트라면 같은 날짜에 지급 내역이 있는지 비교한다.
		if (pointContent.equals("TIL 작성") || pointContent.equals("출석 포인트")) {
			int todayPointCheck = pointService.isTodayPointCheck(member, pointContent);
			// 오늘 TIL이나 출석 포인트를 지급 받지 않았다면
			// 아래와 같은 내용으로 10포인트 지급해준다.
			// (count값이 1 이상이면 이미 포인트를 지급 받은 것)
			if (todayPointCheck < 1) {
				logger.info("PointController addNewPoint() TIL/출석 포인트 지급");
				// 포인트를 지급하고 포인트 테이블에 기록한다.
				pointService.addPointToMember(pointDTO);
				return pointService.addPoint(pointDTO);
			}
		} else if (numOfSearchPointContent < 1) {
			logger.info("PointController addNewPoint() 포인트 지급");
			// 포인트를 지급하고 포인트 테이블에 기록한다.
			pointService.addPointToMember(pointDTO);
			return pointService.addPoint(pointDTO);
		}
		return -1;
	}
	
	// 회원등급 산정
	@ResponseBody
	@RequestMapping(value="/setGrade", method=RequestMethod.POST)
	private int setGrade(HttpSession session, HttpServletRequest req, int workDay) throws Exception {
		logger.info("PointController setGrade()....");
		if(session.getAttribute("member") != null) {
			// 세션에 있는 MemberDTO와 memberId를 저장한다.
			MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
			String memberId = memberDTO.getMemberId();
			// 평일수와 총 포인트를 가지고 등급을 산정한다.
			int memberGrade = 9;
			// 전체 포인트를 가입일 이후의 평일수로 나누기
			int memberAvgPoint = workDay == 0 ? 0 : memberService.getMyPointSum(memberId) / workDay;
			// 평균 포인트를 기준으로 등급 선정
			if(memberAvgPoint > 40) {
				memberGrade = 1;
			} else if(memberAvgPoint > 35) {
				memberGrade = 2;
			} else if(memberAvgPoint > 30) {
				memberGrade = 3;
			} else if(memberAvgPoint > 25) {
				memberGrade = 4;
			} else if(memberAvgPoint > 20) {
				memberGrade = 5;
			} else if(memberAvgPoint > 15) {
				memberGrade = 6;
			} else if(memberAvgPoint > 10) {
				memberGrade = 7;
			} else if(memberAvgPoint > 5) {
				memberGrade = 8;
			} else {
				memberGrade = 9;
			}
			// 기존과 등급에 변화가 있다면 등급을 바꿔준다.
			if (memberGrade != memberDTO.getGrade()) {
				logger.info("PointController setGrade() 바꿀 등급: " + memberGrade);
				pointService.setMemberGrade(memberId, memberGrade);
				// 변경된 등급으로 세션 재발급을 위해 우선 세션을 제거한다.
				session.invalidate();
				// 이후 변경된 grade값이 들어간 세션을 다시 발급해준다.
				session = req.getSession();
				MemberDTO login = memberService.login(memberDTO);
				session.setAttribute("member", login);
				// 등급 변경에 따른 적립금을 지급한다.
				// 등급이 위로 올라갔을 때만 지급한다.
				if (memberGrade < memberDTO.getGrade()) {
					RewardDTO rewardDTO = new RewardDTO();
					String rewardContent = memberGrade + "등급 승급 축하";
					// 적립금 지급액은 등급별로 차등 지급하는데
					// 등급이 높을수록 적립금을 많이 지급한다.
					int changeVal = (10 - memberGrade) * 5000;
					rewardDTO.setContent(rewardContent);
					rewardDTO.setMember(memberId);
					rewardDTO.setChangeVal(changeVal);
					// 같은 내용의 적립금 지급 내역이 있으면 적립금을 지급하지 않는다.
					int numOfSearchRewardContent = rewardService.getNumOfSearchRewardContent(rewardDTO);
					if (numOfSearchRewardContent < 1) {
						logger.info("PointController setGrade() 적립금 지급: " + rewardContent);
						rewardService.addReward(rewardDTO);
						rewardService.addRewardToMember(rewardDTO);
					}
				}
				return memberGrade;
			}
		}
		return -1;
	}
	
}