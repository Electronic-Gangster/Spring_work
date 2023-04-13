package board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import board.dao.AnswerDao;
import board.dao.BoardDao;
import board.dao.MemberDao;
import board.dto.BoardDto;
import board.dto.MemberDto;

@Controller
public class BoardController {
	
	@Autowired
	MemberDao memberDao;
	
	@Autowired
	BoardDao boardDao;
	
	@Autowired
	AnswerDao answerDao;
	
	@GetMapping("board/list")
	public String list(HttpSession session, Model model,
			@RequestParam(defaultValue = "1") int currentPage /*페이지 번호가 안넘어올 경우 무조건 1페이지를 보여준다.*/
			)
	{
		String email=(String)session.getAttribute("loginemail");
		MemberDto dto=memberDao.selectOneOfEmail(email);
		
		model.addAttribute("dto", dto);
		
		//1. 게시판의 총 글 갯수 얻기
		int totalCount=boardDao.getTotalCount();
		int totalPage; 	//총 페이지 수
		int perPage=5; 	//한 페이지당 보여질 글의 갯수
		int perBlock=3; //한 블록당 보여질 페이지의 갯수
		int startNum; 	//각 페이지에서 보여질 글의 시작 번호
		int startPage;	//각 블럭에서 보여질 글의 시작 페이지 번호
		int endPage;	//각 블럭에서 보여길 글의 끝 페이지 번호
		int no;			//글 출력시 출력할 시작 번호
		
		//2. 총페이지 수
		totalPage=totalCount/perPage+(totalCount%perPage==0?0:1); //ex 10/3= 3+1, 9/3=3+0
		
		//3. 시작페이지
		startPage=(currentPage-1)/perBlock*perBlock+1; //ex (2-1)/3*3+1=1, (5-1)/3*3+1=4
		
		//4. 끝페이지
		endPage=startPage+perBlock-1;
		
		//5. 발생하는 문제점
		if(endPage>totalPage) //endPage가 totalPage보다 크면 안된다.
			endPage=totalPage;
		
		//6. 각 페이지에 시작번호 설정
		startNum=(currentPage-1)*perPage; //ex. 1페이지 : 0, 2페이지: 3, 3페이지 : 6
		
		//7. 각 글마다 출력할 글 번호
		no=totalCount-(currentPage-1)*perPage; //ex. 10개일 경우, 1페이지:10, 2페이지: 7...
		
		//8. 각 페이지에 필요한 게시글 db에서 가져오기
		List<BoardDto> list=boardDao.getPagingList(startNum, perPage);
		
		//9. 각 게시글의 작성자 name을 dto에 저장하기
		for(BoardDto bdto:list)
		{
			//9-1. 작성자의 num값
			int num=bdto.getNum();
			
			//9-2. num에 해당하는 작성자 이름
			String name="";
			
			try {
				name=memberDao.selectOneOfNum(num).getName();
			}catch(NullPointerException e) {
				name="탈퇴한회원";		//* num에 해당하는 데이터가 없는 경우, NullPointerException이 발생한다.
			}
			
			//9-3. bdto에 저장
			bdto.setName(name);
			
			//9-4. 댓글 갯수 acount에 저장
			int acount=answerDao.getAllAnswers(bdto.getIdx()).size();
			bdto.setAcount(acount);
		}
		
		//10. 출력시 필요한 변수들을 model에 전부 저장
		
		model.addAttribute("list", list);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("no", no);
		
		return "board/list";
	}
	
	
	@GetMapping("board/form")
	public String form()
	{
		return "board/form";
	}
	
	@PostMapping("/board/addboard")
	public String insertBoard(
			@ModelAttribute BoardDto dto,
			@RequestParam ArrayList<MultipartFile>upload,
			HttpServletRequest request,
			HttpSession session
			)
	{
		//1.세션에 저장된 이메일 얻기
		String email=(String)session.getAttribute("loginemail");
		
		//2.그 이메일에 해당하는 member 테이블의 num값
		int num=memberDao.selectOneOfEmail(email).getNum();
		
		//3.dto에 num값 저장
		dto.setNum(num);
		
		//4.업로드할 경로 지정
		String realPath=request.getSession().getServletContext().getRealPath("/resources/photo");
		String images="";
		
		//5. 업로드 했을 경우, 파일명을 날짜로 변경한다.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fname = sdf.format(new Date());
		
		//6.사진을 업로드 안했을 경우, db에 no로 저장하기
		if(upload.get(0).getOriginalFilename().equals("")) {
			images="no";
		}else {
			int i=0;
			for(MultipartFile mfile:upload)
			{
				String originalName=mfile.getOriginalFilename();
				//.을 기준으로 나눈다(확장자를 분리) 단, 파일명 사이에 . 이 들어간 경우는 안된다.
				StringTokenizer st = new StringTokenizer(originalName, ".");
				String fileName = st.nextToken();
				String extName = st.nextToken();
				System.out.println(fileName+","+extName);
				//파일명을 날짜로 변경하기 (뒤에 인덱스 붙이기)
				fileName=fname+"_"+ i++ +"."+extName;
				System.out.println(fileName); //업로드할 최종 파일
				
				images+=fileName+",";
				
				//7.사진 업로드
				try {
					mfile.transferTo(new File(realPath+"/"+fileName));
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//8.마지막 , 제거
			images=images.substring(0,images.length()-1);
		}
		
		//9.dto에 images값 저장
		dto.setImages(images);
		
		//10.db에 저장
		boardDao.insertBoard(dto);
		
		return "redirect:list";
	}
	
	@GetMapping("/board/content")
	public String content(Model model, @RequestParam int idx, @RequestParam int currentPage)
	{
		//0. 조회수 증가 먼저!
		boardDao.updateReadCount(idx);
		
		//1. board 테이들의 dto를 얻는다.
		BoardDto dto = boardDao.selectOneBoard(idx);
		
		//2. num에 해당하는 name, photo를 얻어서 dto에 넣어준다.
		String name="", photo="";
		
		try{
			name=memberDao.selectOneOfNum(dto.getNum()).getName();
			photo=memberDao.selectOneOfNum(dto.getNum()).getPhoto();
		
			dto.setName(name);
			dto.setPhoto(photo);
		}catch(NullPointerException e) 
		{
			dto.setName("탈퇴한회원");
			dto.setPhoto("noimage.png");
		}
		//3. model에 dto, currentPage를 저장한다.
		model.addAttribute("dto", dto);
		model.addAttribute("currentPage", currentPage);
		
		return "board/content";
	}
	
	@GetMapping("/board/delete")
	public String delete(@RequestParam int idx, @RequestParam int currentPage)
	{
		//1. 삭제 후 보던 페이지 목록으로 이동
		boardDao.deleteBoard(idx);
		
		return "redirect:list?currentPage="+currentPage;
	}
	
	@GetMapping("/board/updateform")
	public String updateForm(Model model, @RequestParam int idx, @RequestParam int currentPage)
	{
		//idx에 해당하는 dto 얻기
		BoardDto dto = boardDao.selectOneBoard(idx);
		
		model.addAttribute("dto", dto);
		model.addAttribute("currentPage", currentPage);
		
		return "board/updateform";
	}
	
	@PostMapping("/board/updateboard")
	public String updateBoard(
			@ModelAttribute BoardDto dto,
			@RequestParam ArrayList<MultipartFile>upload,
			@RequestParam int currentPage,
			HttpServletRequest request
			)
	{
		
		//1.업로드할 경로 지정
		String realPath=request.getSession().getServletContext().getRealPath("/resources/photo");
		String images="";
		
		//2. 업로드 했을 경우, 파일명을 날짜로 변경한다.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fname = sdf.format(new Date());
		
		//3.사진을 업로드 안했을 경우, db에 null로 저장하기
		if(upload.get(0).getOriginalFilename().equals("")) {
			images=null;	//sql에서 null 값을 줬기 때문. 기존 사진이 유지 된다.
		}else {
			int i=0;
			for(MultipartFile mfile:upload)
			{
				String originalName=mfile.getOriginalFilename();
				//.을 기준으로 나눈다(확장자를 분리) 단, 파일명 사이에 . 이 들어간 경우는 안된다.
				StringTokenizer st = new StringTokenizer(originalName, ".");
				String fileName = st.nextToken();
				String extName = st.nextToken();
				System.out.println(fileName+","+extName);
				//파일명을 날짜로 변경하기 (뒤에 인덱스 붙이기)
				fileName=fname+"_"+ i++ +"."+extName;
				System.out.println(fileName); //업로드할 최종 파일
				
				images+=fileName+",";
				
				//4.사진 업로드
				try {
					mfile.transferTo(new File(realPath+"/"+fileName));
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//5.마지막 , 제거
			images=images.substring(0,images.length()-1);
		}
		
		//7.dto에 images값 저장
		dto.setImages(images);
		
		//8.db에 수정
		boardDao.updateBoard(dto);
		
		//9.내용보기로 이동
		return "redirect:content?idx="+dto.getIdx()+"&currentPage="+currentPage;
	}
	
}
