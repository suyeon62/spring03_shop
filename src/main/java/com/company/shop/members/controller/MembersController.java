package com.company.shop.members.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.shop.members.dto.AuthInfo;
import com.company.shop.members.dto.MembersDTO;
import com.company.shop.members.service.MembersService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@CrossOrigin(origins={"http://localhost:3000"})
@CrossOrigin("*")
@RestController
public class MembersController {
	
	@Autowired
	private MembersService membersService;
	
	@Autowired
	private BCryptPasswordEncoder encodePassword;
	
	public MembersController() {
		
	}
	
	//회원가입 처리
	@PostMapping("/member/signup")
	public ResponseEntity<AuthInfo> addmember(@RequestBody MembersDTO membersDTO){
		log.info("membersDTO:{}:", membersDTO);
		membersDTO.setMemberPass(encodePassword.encode(membersDTO.getMemberPass()));
		AuthInfo authInfo=membersService.addMemberProcess(membersDTO);
		return ResponseEntity.ok(authInfo);
	}
	
	//회원정보 수정
	@PutMapping("/member/update")
	public ResponseEntity<AuthInfo> updateMember(@RequestBody MembersDTO memberDTO){
		return null;
	}

} //end class
