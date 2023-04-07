package board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
	
	@GetMapping("board/list")
	public String list()
	{
		return "board/list";
	}
	
	@GetMapping("board/form")
	public String form()
	{
		return "board/form";
	}
}
