package com.company.shop.members.dto;

import com.company.shop.common.exception.WrongEmailPasswordException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembersDTO {
	private String memberEmail; //이메일
	private String memberPass; //비밀번호
	private String memberName; //이름
	private String memberPhone; //전화번호
	private int memberType=1; //회원구분 일반회원 1, 관리자 2
	private String authRole;
	
	public boolean matchPassword(String memberPass) {
		return this.memberPass.equals(memberPass);
	}

	public void changePassword(String oldPassword, String newPassword) {
		if (!this.memberPass.equals(oldPassword))
			throw new WrongEmailPasswordException("비밀번호 불일치");
		this.memberPass = newPassword;
	}

}
