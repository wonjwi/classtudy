package com.edu.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.domain.NotiDTO;
import com.edu.service.NotiService;

@Controller // 컨트롤러 빈으로 등록하는 어노테이션
@RequestMapping("/noti/*") // NotiController에서 공통적으로 사용될 url mapping
public class NotiController {
	
	//로깅을 위한 변수 logger를 선언한다.
	private static final Logger logger = LoggerFactory.getLogger(NotiController.class);
	
	@Inject
	NotiService notiService;
	
	// 알림 개수 읽어오기
	@ResponseBody
	@RequestMapping(value="/load", method=RequestMethod.POST)
	private int notiLoad(String memberId) throws Exception {
		logger.info("NotiController notiCheck()....");
		return notiService.notiLoad(memberId);
	}
	
	// 알림 목록 보기
	@ResponseBody
	@RequestMapping(value="/list/{memberId}", method=RequestMethod.POST)
	private List<NotiDTO> notiList(@PathVariable String memberId, Model model) throws Exception {
		logger.info("NotiController notiList()....");
		return notiService.notiList(memberId);
	}
	
	// 알림 추가
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	private int notiInsert(String notiContent, String receiver) throws Exception {
		logger.info("NotiController notiInsert()....");
		
		NotiDTO notiDTO = new NotiDTO();
		notiDTO.setContent(notiContent);
		notiDTO.setReceiver(receiver);
		notiDTO.setChecked(false);
		
		return notiService.notiInsert(notiDTO);
	}
	
	// 알림 확인
	@ResponseBody
	@RequestMapping(value="/check/{notiNo}", method=RequestMethod.POST)
	private int notiCheck(@PathVariable int notiNo, Model model) throws Exception {
		return notiService.notiCheck(notiNo);
	}

}