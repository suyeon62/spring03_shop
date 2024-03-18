package com.company.shop.members.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.shop.members.dto.AuthInfo;
import com.company.shop.members.dto.ChangePwdCommand;
import com.company.shop.members.dto.MembersDTO;
import com.company.shop.members.repository.MembersRepository;

@Service
public class MembersServiceImp implements MembersService{
	@Autowired
	private MembersRepository membersRepository;
	
	public MembersServiceImp() {
		
	}

	@Override
	public AuthInfo addMemberProcess(MembersDTO dto) {
		membersRepository.insertMember(dto);
		return new AuthInfo(dto.getMemberEmail(),dto.getMemberName(),dto.getMemberPass());
	}

	@Override
	public AuthInfo loginProcess(MembersDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MembersDTO updateMemberProcess(String memberEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthInfo updateMemberProcess(MembersDTO dto) {
		membersRepository.updateMember(dto);
		return new AuthInfo(dto.getMemberEmail(),dto.getMemberName(),dto.getMemberPass());
	}

	@Override
	public void updatePassProcess(String memberEmail, ChangePwdCommand changePwd) {
		// TODO Auto-generated method stub
		
	}

}
