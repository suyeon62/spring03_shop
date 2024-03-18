package com.company.shop.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.shop.members.dto.MembersDTO;
import com.company.shop.members.repository.MembersRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	@Autowired
	private MembersRepository membersRepository;

	@Override
	public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
		log.info("PrincipalService username:{}", memberEmail);
		
		MembersDTO membersDTO=membersRepository.selectByEmail(memberEmail);
		log.info("memberEmail:{} memberPass:{} memberName:{}",membersDTO.getMemberEmail(), membersDTO.getMemberPass(), membersDTO.getMemberName());
		
		if(membersDTO==null) {
			throw new UsernameNotFoundException(memberEmail);
		}
		return null;
	}


}
