package board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import board.dao.AnswerDao;
import board.dao.MemberDao;

@RestController
public class AnswerController {
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	AnswerDao answerDao;
	
}
