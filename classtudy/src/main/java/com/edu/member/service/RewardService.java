package com.edu.member.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.edu.member.domain.RewardDTO;
import com.edu.member.mapper.RewardMapper;

@Service("com.edu.member.service.RewardService")
public class RewardService {
	
	@Resource(name="com.edu.member.mapper.RewardMapper")
	RewardMapper rewardMapper;
	
	// 중복된 적립금 지급 내역이 있는지 확인
	public int getNumOfSearchRewardContent(RewardDTO rewardDTO) throws Exception {
		return rewardMapper.getNumOfSearchRewardContent(rewardDTO);
	}
	// 적립금 지급 내역 추가
	public int addReward(RewardDTO rewardDTO) throws Exception {
		return rewardMapper.addReward(rewardDTO);
	}
	// 회원에게 적립금 지급
	public int addRewardToMember(RewardDTO rewardDTO) throws Exception {
		return rewardMapper.addRewardToMember(rewardDTO);
	}
	
}