package com.edu.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.edu.domain.NotiDTO;

@Repository("com.edu.mapper.NotiMapper")
public interface NotiMapper {
	
	// 알림 개수 읽어오기
	public int notiLoad(String memberId) throws Exception;
	
	// 알림 목록 보기
	public List<NotiDTO> notiList(String memberId) throws Exception;
	
	// 알림 추가
	public int notiInsert(NotiDTO notiDTO) throws Exception;
	
	// 알림 확인
	public int notiCheck(int notiNo) throws Exception;
	
}