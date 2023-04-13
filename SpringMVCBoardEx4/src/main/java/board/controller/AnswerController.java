package board.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import board.dao.AnswerDao;
import board.dao.MemberDao;
import board.dto.AnswerDto;

@RestController
public class AnswerController {
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	AnswerDao answerDao;
	
	//1. 댓글 insert
	@GetMapping("/board/ainsert")
	public void insertAnswer(@RequestParam String content, @RequestParam int idx, @RequestParam int num)
	{
		//1-1. dto에 담는다.
		AnswerDto dto=new AnswerDto();
		dto.setContent(content);
		dto.setIdx(idx);
		dto.setNum(num);
		
		answerDao.insertAnswer(dto);
	}
	
	//2. 댓글 전체 리스트
	@GetMapping("/board/alist")
	public List<AnswerDto> getAllAnswers(@RequestParam int idx)
	{
		List<AnswerDto> list=answerDao.getAllAnswers(idx); //list가 json형태로 반환 (맨위, RestController 때문)
		for(AnswerDto dto:list)
		{
			//2-1. member 테이블로 부터 name, photo를 얻어서 dto에 넣는다.
			try {
				String name=memberDao.selectOneOfNum(dto.getNum()).getName();
				String photo=memberDao.selectOneOfNum(dto.getNum()).getPhoto();
				
				dto.setName(name);
				dto.setPhoto(photo);
				
			}catch(NullPointerException e) {
				//2-2. 해당 num이 없을 경우 널포인터 익셉션이 발생
				dto.setName("탈퇴한회원");
				dto.setPhoto("noimage.png");
			}
		}
		return list;
	}
	
	//3. 댓글 삭제
	@GetMapping("/board/adelete")
	public void deleteAnswer(@RequestParam int seq)
	{
		answerDao.deleteAnswer(seq);
	}
}
