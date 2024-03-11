package com.company.shop.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.shop.board.dto.BoardDTO;
import com.company.shop.board.dto.PageDTO;
import com.company.shop.board.repository.BoardRepository;

@Service
public class BoardServiceImp implements BoardService{
	@Autowired
	private BoardRepository boardRepository;
	
	public BoardServiceImp() {
	}

	@Override
	public int countProcess() {
		return boardRepository.count();
	}

	@Override
	public List<BoardDTO> listProcess(PageDTO pv) {
		return boardRepository.list(pv);
	}

	@Override
	public void insertProcess(BoardDTO dto) {
		int refPlus=boardRepository.refPlus();
		System.out.println("refPlus"+refPlus);
		dto.setRef(refPlus);
		boardRepository.save(dto);
	}

	@Override
	public BoardDTO contentProcess(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProcess(BoardDTO dto, String urlpath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProcess(int num, String urlpath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String fileSelectprocess(int num) {
		// TODO Auto-generated method stub
		return null;
	}
}
