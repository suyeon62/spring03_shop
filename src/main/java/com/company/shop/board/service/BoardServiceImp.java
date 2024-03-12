package com.company.shop.board.service;

import java.io.File;
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
		//제목글이면
		if(dto.getRef()==0) {
			int refPlus=boardRepository.refPlus();
			dto.setRef(refPlus);
		}else {
			//답변글이면
			boardRepository.reStepCount(dto);
			dto.setRe_step(dto.getRe_step()+1);
			dto.setRe_level(dto.getRe_level()+1);
		}
		boardRepository.save(dto);
	}

	@Override
	public BoardDTO contentProcess(int num) {
		boardRepository.readCount(num);
		return boardRepository.content(num);
	}

	@Override
	public void updateProcess(BoardDTO dto, String urlpath) {
		String filename=dto.getUpload();
		//수정할 첨부파일이 있으면
		if(filename!=null) {
			String path=boardRepository.getFile(dto.getNum());
			//기존에 저장된 첨부파일이 있으면
			if(path!=null) {
				File file=new File(urlpath,path);
				file.delete();
			}
		}
		boardRepository.update(dto);
	}

	@Override
	public void deleteProcess(int num, String urlpath) {
		String path=boardRepository.getFile(num);
		//첨부파일 있으면
		if(path!=null) {
			File file=new File(urlpath, path);
			file.delete();
		}
		boardRepository.delete(num);
	}


}
