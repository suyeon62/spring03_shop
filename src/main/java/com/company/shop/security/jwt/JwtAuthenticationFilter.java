package com.company.shop.security.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.company.shop.members.dto.MembersDTO;
import com.company.shop.security.service.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//Authentication(인증)
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authManager;
	private Authentication authentication;
	
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
			
			authentication=authManager.authenticate(authenticationToken);
			log.info("authentication:{}",authentication.getDetails());
			log.info("authentication:{}",authentication.getPrincipal());
			
			PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();
			log.info("로그인 완료됨(인증):{}, {}",principalDetails.getUsername(),principalDetails.getPassword());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return authentication;
	}
	
	//attemptAuthentication() 실행 후 인증이 정상적으로 완료되면 실행된다.
	//여기에서 JWT(Json Web Token) 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해준다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("successfulAuthentication 실행됨");
		
		PrincipalDetails principalDetails=(PrincipalDetails)authResult.getPrincipal();
		
		String jwtToken=JWT.create()
				.withSubject("mycors")
				.withExpiresAt(new Date(System.currentTimeMillis()+(60*60*1000*1L))) //만료시간
				.withClaim("memberPass",principalDetails.getPassword())
				.withClaim("memberEmail",principalDetails.getUsername())
				.sign(Algorithm.HMAC512("mySecurityCos"));
		
		log.info("jwtToken:{}", jwtToken);
		
		//response 응답헤더에 jwtToken 추가
		response.addHeader("Authorization", "Bearer "+jwtToken);
		
		final Map<String, Object> body=new HashMap<>();
		body.put("memberName", principalDetails.getMembersDTO().getMemberName());
		body.put("memberEmail", principalDetails.getMembersDTO().getMemberEmail());
		
		ObjectMapper mapper=new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		System.out.println("unsuccessfulAuthentication 실행됨");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", HttpStatus.UNAUTHORIZED.value());
        body.put("error", failed.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), body);
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
	}

} //end class()
