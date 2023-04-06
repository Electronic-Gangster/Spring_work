package bit.study.spring;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//자동으로 xml에 bean을 등록
@Controller
public class HomeController {
	
	//@RequestMapping(value = "/", method = RequestMethod.GET)
	//@RequestMapping("/")
	
	@GetMapping("/") //5.0 버전 부터 가능
	public String home(Model model, HttpSession session)
	{
		//session에 저장
		session.setAttribute("myid", "angel");
		
		//request에 저장
		model.addAttribute("message", "Have a Good Trip!!!");
		model.addAttribute("today", new Date());
		
		
		return "home"; 		//<- 포워드 할 파일명 /WEB-INF/views/home.jsp
	}
	
	//@GetMapping("/myshop")
	@GetMapping({"/myshop","/yourshop"}) //매핑 여러개 적용시
	public ModelAndView goShop()
	{
		ModelAndView model = new ModelAndView();
		
		//request에 저장
		model.addObject("sangpum", "박정희 초상화");
		model.addObject("price", "5000원");
		model.addObject("color", "yellow");
		model.addObject("photo", "3.jpg");
		
		//포워드
		model.setViewName("myshop");
		return model;
	}
	
}
