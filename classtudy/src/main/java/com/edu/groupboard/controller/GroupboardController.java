package com.edu.groupboard.controller;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.common.CommonUtils;
import com.edu.member.domain.MemberDTO;

@Controller // 컨트롤러 빈으로 등록
@RequestMapping("/group/*") // GroupboardController에서 공통적으로 사용될 url mapping
public class GroupboardController {
	
	//로깅을 위한 변수 logger를 선언한다.
	private static final Logger logger = LoggerFactory.getLogger(GroupboardController.class);
	
	//@Inject
	//GroupboardService groupboardService;
	
	@Inject
	CommonUtils commonUtils;
	
	// 게시판 목록 보기
	@RequestMapping(value={"/board/{viewCategory}", "/board/{viewCategory}/{pageNum}"})
	private String list(@PathVariable String viewCategory, @PathVariable Optional<Integer> pageNum, 
			HttpSession session, Model model, RedirectAttributes rttr) throws Exception {
		logger.info("GroupboardController list()....");
		// 로그인을 하지 않았으면 로그인 화면으로 보낸다.
		if (session.getAttribute("member") == null) {
			rttr.addFlashAttribute("msgLogin", false);
			return "redirect:/member/login";
		}
		// session에서 memberDTO를 저장한다.
		MemberDTO member = (MemberDTO)session.getAttribute("member");
		// memberDTO에서 그룹번호를 찾아 저장한다.
		int groupNo1 = member.getGroup1();
		int groupNo2 = member.getGroup2();
		int groupNo3 = member.getGroup3();
		// memberDTO에서 아이디를 찾아 저장한다.
		String memberId = member.getMemberId();
		logger.info("GroupboardController list(): " + memberId + "(" + groupNo1 + ", " + groupNo2 + ", " + groupNo3 + ")");
		
		if ((groupNo1 == 1 || groupNo1 == 0) && (groupNo2 == 1 || groupNo2 == 0) && (groupNo3 == 1 || groupNo3 == 0)) {
			model.addAttribute("message", "그룹에 가입하신 후 다시 시도해주세요.");
			return "/common/noAccess";
		} else {
			return "/board/listGroupboard";
		}
	}
}