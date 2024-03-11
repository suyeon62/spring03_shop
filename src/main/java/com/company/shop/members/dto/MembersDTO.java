package com.company.shop.members.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembersDTO {
	private String memberEmail; //이메일
	private String memberPass; //비밀번호
	private String memberName; //이름
	private String memberPhone; //전화번호
	private int memberType; //회원구분 일반회원 1, 관리자 2

}
