package com.company.shop.members.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.company.shop.members.dto.MembersDTO;


@Mapper
@Repository
public interface MembersRepository {
	public int insertMember(MembersDTO dto);
	public MembersDTO selectByEmail(String memberEmail);
	
	public void updateMember(MembersDTO dto);
	public void updateByPass(MembersDTO dto);

}
