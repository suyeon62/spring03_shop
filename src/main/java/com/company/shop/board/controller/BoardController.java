package com.company.shop.board.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.shop.board.dto.BoardDTO;
import com.company.shop.board.dto.PageDTO;
import com.company.shop.board.service.BoardService;
import com.company.shop.common.file.FileUpload;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin("*")
@Slf4j
@RestController
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private PageDTO pdto;
	private int currentPage;
	
	@Value("${spring.servlet.multipart.location}")
	private String filePath;
	
	public BoardController() {
		
	}
	
	//http://localhost:8090/board/list/1
	
	@GetMapping("/board/list/{currentPage}")
	public ResponseEntity<Map<String, Object>> listExecute(@PathVariable("currentPage") int currentPage){
		Map<String, Object> map=new HashMap<>();
		int totalRecord=boardService.countProcess();
		log.info("totalRecord:{}",totalRecord);
		
		if(totalRecord>=1) {
			this.currentPage=currentPage;
			this.pdto=new PageDTO(this.currentPage,totalRecord);
			
			map.put("boardList", boardService.listProcess(pdto));
			map.put("pv", this.pdto);
		}
		
		log.info("boardList{}", map.get("boardList"));
		return ResponseEntity.ok(map);
	} //end listExecute()
	
	//첨부파일이 있을 때 @RequestBody를 선언하면 안 된다.
	//답변글일때 ref, re_step, re_level 담아서 넘겨야 한다.
	@PostMapping("/board/write")
	public ResponseEntity<String> writeProExecute(BoardDTO dto, PageDTO pv, HttpServletRequest req, HttpSession session) {
		MultipartFile file=dto.getFilename();
		log.info("subject:{}, content:{}", dto.getSubject(), dto.getContent());
		//log.info("file:{}",file.getOriginalFilename());
		
		//파일 첨부가 있으면
		if(file!=null && !file.isEmpty()) {
			UUID random=FileUpload.saveCopyFile(file,filePath);
			dto.setUpload(random+"_"+file.getOriginalFilename());
		}
		
		dto.setIp(req.getRemoteAddr());
		boardService.insertProcess(dto);
		return ResponseEntity.ok(String.valueOf(1));
	}
	
	@GetMapping("/board/view/{num}")
	public ResponseEntity<BoardDTO> viewExecute(@PathVariable("num") int num) {
		BoardDTO boardDto=boardService.contentProcess(num);
		return ResponseEntity.ok(boardDto);
	}
	
	@PutMapping("/board/update")
	public ResponseEntity<Object> updateExecute(BoardDTO dto, HttpServletRequest req){
		MultipartFile file=dto.getFilename();
		if(file!=null && !file.isEmpty()) {
			UUID random=FileUpload.saveCopyFile(file, filePath);
			dto.setUpload(random+"_"+file.getOriginalFilename());
		}
		boardService.updateProcess(dto, filePath);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/board/delete/{num}")
	public ResponseEntity<Object> deleteExecute(@PathVariable("num") int num){
		boardService.deleteProcess(num, filePath);
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/board/contentdownload/{filename}")
	public ResponseEntity<Resource> downloadExecute(@PathVariable("filename") String filename) throws IOException{
		String fileName=filename.substring(filename.indexOf("_")+1);
		
		//파일명이 한글일 때 인코딩 작업을 한다.
		String str=URLEncoder.encode(fileName,"UTF-8");
		
		//원본 파일명에 공백이 있을 때, "+" 표시가 되므로 공백으로 처리해줌
		str=str.replaceAll("\\+", "%20");
		
		Path path=Paths.get(filePath+"\\"+filename);
		Resource resource=new InputStreamResource(Files.newInputStream(path));
		log.info("resource:{}", resource);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+str+";")
				.body(resource);
	}
} //end class

/*
 *<bean id="boardService" class "com.company.shop.board.service.BoardService" />
 *<bean id="boardController" class "com.company.shop.board.controller" />
 *	<constructor ref="boardService" />
 *</bean>
 */
