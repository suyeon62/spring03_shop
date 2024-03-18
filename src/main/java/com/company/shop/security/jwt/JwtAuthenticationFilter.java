package com.company.shop.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.company.shop.members.dto.MembersDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//Authentication(인증)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authManager) {
		this.authManager=authManager;
	}
	

	//http://localhost:8090/login 요청을 하면 실행되는 메소드
	@Override
	public  Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
		System.out.println("JwtAuthenticationFilter => login 요청 처리를 시작함");
		try {
//			BufferedReader br=request.getReader();
//			String input=null;
//			while((input=br.readLine())!=null)
//				System.out.println(input);
			
			//{"memberEmail":"love@google.com", "memberPass":"1234"}
			//스트림을 통해서 읽어온 json을 MemberDTO로 객체로 변경
			ObjectMapper om=new ObjectMapper();
			MembersDTO membersDTO=om.readValue(request.getInputStream(), MembersDTO.class);
			log.info("memberEmail:{}, memberPass:{}", membersDTO.getMemberEmail(), membersDTO.getMemberPass());
			
			UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(membersDTO.getMemberEmail(),membersDTO.getMemberPass());
			
			Authentication authentication=authManager.authenticate(authenticationToken);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

} //end class()
