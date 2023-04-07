package board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import board.dao.MemberDao;

@Controller
public class MemberController {
	
	@Autowired
	private MemberDao memberDao;
	
	@GetMapping("/member/form")
	public String form(Model model)
	{
		//가입 된 총 인원수를 폼 위에 출력하기 위해, 값을 얻는다.
		int totalCount=memberDao.getTotalCount();
		
		//request에 저장
		model.addAttribute("totalCount", totalCount);
		
		return "member/form";
	}
	
}
