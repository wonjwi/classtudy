package com.edu.member.mapper;

import org.springframework.stereotype.Repository;

import com.edu.member.domain.RewardDTO;

@Repository("com.edu.member.mapper.RewardMapper")
public interface RewardMapper {
	
	// 중복된 적립금 지급 내역이 있는지 확인
	public int getNumOfSearchRewardContent(RewardDTO rewardDTO) throws Exception;
	// 적립금 지급 내역 추가
	public int addReward(RewardDTO rewardDTO) throws Exception;
	// 회원에게 적립금 지급
	public int addRewardToMember(RewardDTO rewardDTO) throws Exception;
	
}