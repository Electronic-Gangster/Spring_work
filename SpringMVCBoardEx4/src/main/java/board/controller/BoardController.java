package board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import board.dao.BoardDao;
import board.dao.MemberDao;
import board.dto.MemberDto;

@Controller
public class BoardController {
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	BoardDao boardDao;
	
	@GetMapping("board/list")
	public String list(HttpSession session, Model model)
	{
		String email=(String)session.getAttribute("loginemail");
		MemberDto dto=memberDao.selectOneOfEmail(email);
		
		model.addAttribute("dto", dto);
		
		//1. 게시판의 총 글 갯수 얻기
		int totalCount=boardDao.getTotalCount();
		model.addAttribute("totalCount", totalCount);
		
		return "board/list";
	}
	
	@GetMapping("board/form")
	public String form()
	{
		return "board/form";
	}
}
