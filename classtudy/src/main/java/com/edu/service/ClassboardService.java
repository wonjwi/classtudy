package com.edu.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.edu.controller.MemberController;
import com.edu.domain.ClassboardDTO;
import com.edu.mapper.ClassboardMapper;

@Service("com.edu.service.ClassboardService")
public class ClassboardService {
	
	// 로깅을 위한 변수 LOGGER를 선언한다.
	private static final Logger logger
		= LoggerFactory.getLogger(MemberController.class);
	
	@Resource(name="com.edu.mapper.ClassboardMapper")
	ClassboardMapper classboardMapper;
	
	// TIL 작성
	public int writeTIL(ClassboardDTO cbDTO) throws Exception {
		logger.info("Service writeTIL : " + cbDTO);
		return classboardMapper.writeTIL(cbDTO);
	}
	
	// 게시판 목록 보기
	public List<ClassboardDTO> boardList(int lectureNo) throws Exception {
		return classboardMapper.boardList(lectureNo);
	}
	
	// 게시글 상세 보기
	public ClassboardDTO boardDetail(int boardNo) throws Exception {
		return classboardMapper.boardDetail(boardNo);
	}
	
	// 게시글 조회수 증가
	public int addViews(int boardNo) throws Exception {
		return classboardMapper.addViews(boardNo);
	}
	
}
