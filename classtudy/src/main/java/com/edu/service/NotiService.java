package com.edu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.edu.domain.NotiDTO;
import com.edu.mapper.NotiMapper;

@Service("com.edu.service.NotiService")
public class NotiService {
	
	@Resource(name="com.edu.mapper.NotiMapper")
	NotiMapper notiMapper;
	
	// 알림 개수 읽어오기
	public int notiLoad(String memberId) throws Exception {
		return notiMapper.notiLoad(memberId);
	}
	
	// 알림 목록 보기
	public List<NotiDTO> notiList(String memberId) throws Exception {
		return notiMapper.notiList(memberId);
	}
	
	// 알림 추가
	public int notiInsert(NotiDTO notiDTO) throws Exception {
		return notiMapper.notiInsert(notiDTO);
	}
	
	// 알림 확인
	public int notiCheck(int notiNo) throws Exception {
		return notiMapper.notiCheck(notiNo);
	}
	
}