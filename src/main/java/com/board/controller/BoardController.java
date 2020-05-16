package com.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.domain.BoardDTO;
import com.board.service.BoardService;

@Controller // 해당 클래스가 사용자의 요청과 응답을 처리하는, 즉 UI를 담당하는 컨트롤러 클래스임을 의미
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	/**
	 * 이전 버전의 스프링에서는 컨트롤러 메서드에 URI를 매핑할 때 
	 * @RequestMapping에 value와 method 속성을 지정해서 사용
	 *  스프링 4.3 버전부터 @GetMapping, @PostMapping, @PutMapping
	 *  , @PatchMapping, @DeleteMapping 등 요청 메서드의 타입별로 매핑을 쉽고 효율적으로 할 수 있는 애너테이션들이 추가
	 *  ex)  기존의 URI 매핑 : @RequestMapping(value = "...", method = RequestMethod.POST)
	 *         현재의 URI 매핑 : @PostMapping(value = "...")
	 *         
	 *   리턴 타입
	 *         컨트롤러에서 메서드의 리턴 타입은 void, String, ModelAndView 등 여러 가지가 존재
	 *         String과 ModelAndView는 리턴할 뷰(HTML 파일)의 이름을 지정
	 *         ModelAndView는 예전에는 많이 사용되었지만 지금은 String을 많이 선호
	 *         컨트롤러의 openBoardWrite 메서드에서 return 문은 HTML 파일의 경로를 의미하고
	 *         , board/write 뒤에는 suffix(접미사)로. html이 자동붙게된다.
	 *         즉, board/write.html을 의미
	 *   
	 *   Model 
	 *       메서드 파라미터 영역의 Model 인터페이스는 데이터를 뷰로 전달할 때 사용
	 * @param model
	 * @return
	 */
	@GetMapping(value="board/write.do")
	public String openBoardWrite(@RequestParam(value="idx", required = false) Long idx, Model model) {
		
//		String title = "제목";
//		String content = "내용";
//		String writer = "홍길동";
//		
//		//Model 인터페이스를 컨트롤러 메서드의 파라미터로 지정하고, 해당 메서드를 사용해서 addAttribute(key, Value) 형태로 데이터를 전달(일반적으로 Key와 Value를 같은 이름으로 지정해서 사용)
//		model.addAttribute("t", title);
//		model.addAttribute("c", content);
//		model.addAttribute("w", writer);
		
		if (idx == null) {
			model.addAttribute("board", new BoardDTO());
		} else {
			BoardDTO board = boardService.getBoardDetail(idx);
			if (board == null) {
				return "redirect:/board/list.do";
			}
			model.addAttribute("board",board);
		}
		return "board/write";
	}
	
	@PostMapping(value="/board/register.do")
	public String regiserBoard(final BoardDTO params) {
		
		try {
			boolean isRegistered = boardService.registerBoard(params);
			if (isRegistered == false ) {
				//
			}
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return "redirect:/board/list.do";
	}
}
