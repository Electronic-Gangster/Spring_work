package board.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import board.dto.AnswerDto;
import board.dto.BoardDto;
import board.dto.MemberDto;

@Repository
public class AnswerDao {

	@Autowired
	private SqlSession session;
	
	String nameSpace="board.dao.AnswerDao.";
	
	
	public void insertAnswer (AnswerDto dto)
	{
		session.insert(nameSpace+"insertOfAnswer", dto);
	}
	
	
	public List<AnswerDto> getAllAnswers(int idx)
	{
		return session.selectList(nameSpace+"selectAllOfAnswer",idx);
	}
	
	
	public void deleteAnswer(int seq)
	{
		session.delete(nameSpace+"deleteOfAnswer", seq);
	}
	
	
	
}
