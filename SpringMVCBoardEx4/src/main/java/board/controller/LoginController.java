package board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import board.dao.MemberDao;

@Controller
public class LoginController {
	
	@Autowired
	MemberDao memberDao;
	
	@GetMapping("login/login")
	public String login(HttpSession session)
	{
		//1. 로그인 여부를 판단할 세션 - 없을 경우 "null"
		String loginok=(String)session.getAttribute("loginok");
		
		if(loginok==null)
			return "login/login";
		else
			return "login/logout";
	}
	
	@PostMapping("/login/loginaction")
	public String loginaction(
			@RequestParam String email,
			@RequestParam String pass,
			@RequestParam(required = false) String saveemail, /* 체크 안 했을 때 null 값으로 넘어온다.*/
			//@RequestParam(defaultValue = "no") String saveemail, /*체크 안 했을 경우 no 값으로 변환되서 넘어온다.*/
			HttpSession session
			)
	{
		//1. 이메일과 비번이 맞는지 체크
		int count=memberDao.isEqualPassEmail(email, pass);
		if(count==1) //이메일과 비번이 맞는 경우 1이 반환
		{
			//2. 로그인 성공시 세션에 저장하기
			session.setAttribute("loginok", "yes");
			session.setAttribute("loginemail", email);
			session.setAttribute("saveemail", saveemail==null?"no":"yes");
			
			//3. 로그인 한사람의 num값을 얻어서 세션에 저장하기
			int num=memberDao.selectOneOfEmail(email).getNum();
			session.setAttribute("loginnum", num);
			
			return "redirect:../board/list";
			
		}else {
			//4. 실패시 loginfail.jsp로 가서 자바스크립트 코드 실행
			return "login/loginfail";
		}
	}
	
	@GetMapping("/login/logout")
	public String logout(HttpSession session)
	{
		session.removeAttribute("loginok");
		return "redirect:login";
	}
}
