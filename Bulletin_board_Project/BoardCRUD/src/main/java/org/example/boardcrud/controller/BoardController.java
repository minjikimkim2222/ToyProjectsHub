package org.example.boardcrud.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.boardcrud.domain.Board;
import org.example.boardcrud.repository.BoardRepository;
import org.example.boardcrud.service.BoardService;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 1. 게시글 조회 - /list
    @GetMapping("/list")
    public String showBoardList(Model model,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int pageSize,
    @RequestParam(defaultValue = "latest") String sort
    ){
        // 최신순, 좋아요순 정렬 처리
        Pageable pageable;

        if (sort.equals("latest")){ // 최신순
            pageable = PageRequest.of(page - 1,
                    pageSize, Sort.by(Sort.Direction.DESC, "created_at"));
        } else { // 좋아요순
            pageable = PageRequest.of(page - 1,
                    pageSize, Sort.by(Sort.Direction.DESC, "likes"));
        }

        Page<Board> boards = boardService.findBoardsWithPagingAndSort(pageable);

        model.addAttribute("boards", boards);
        // 페이징 처리 중, 현재 페이지일 때 "active" 속성을 주기 위해..
        model.addAttribute("currentPage", page);

        return "list";
    }

    // 2. 게시글 상세조회 - /view?id=아이디
    // /view?id=12
    @GetMapping("/view")
    public String findListById(@RequestParam int id, Model model){
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);

        return "view";
    }

    // 3. 게시글 등록 폼
    // /writeForm - get - 등록 폼 보여주세요
    // /wirteForm - post - 진짜로 등록해주세요
    @GetMapping("/writeform")
    public String showWriteForm(Model model){
        model.addAttribute("board", new Board());
        return "write";
    }
    // 검증조건 추가
    @PostMapping("/writeform")
    public String writeAndValidateBoard(@Valid @ModelAttribute("board") Board board, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "write"; // 검증실패했다면, 다시 write뷰로 가서 입력하도록..

        // 해당 날짜값을 디폴트로 설정해야, 가장 최근데이터값이 제일 먼저 조회된다.
        LocalDateTime now = LocalDateTime.now();
        board.setCreated_at(now);
        board.setUpdated_at(now);
        boardService.saveBoard(board);
        return "redirect:/list";
    }

    // 참고로, 게시글 수정/삭제 기능은 사용자가 입력한 암호가 해당 board의 암호와 맞아야만 이뤄진다..
    // 4. 게시글 삭제 -- 참고로
    // /deleteform?id=아이디 -- GET -- 특정 게시글 삭제 위한 폼 제공
    @GetMapping("/deleteform")
    public String showDeleteForm(@RequestParam int id, Model model){
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "delete";
    }
    // /deleteform  -- POST - 진짜 삭제 기능
    // 이때, 사용자가 입력한 암호와, 해당 보드의 암호가 일치해야만 삭제 가능!
    @PostMapping("/deleteform")
    public String deleteBoard(@ModelAttribute("board") Board board, RedirectAttributes redirectAttributes){

        String realPassword = boardService.findBoardById(board.getId()).getPassword();

        if (realPassword.equals(board.getPassword())){ // 사용자가 입력한 암호와, 해당 암호가 일치한다면, 삭제 가능
            boardService.deleteBoard(board.getId());
            return "redirect:/list";
        } else {
            // 비밀번호가 틀렸다! 다시 비밀번호 입력해주세요.. + 에러메시지 전달
            redirectAttributes.addAttribute("errorMsg", true);
            return "redirect:/deleteform?id=" + board.getId();
        }
    }

    // 5. 같은 방식의 게시글 수정
    // /updateform?id=아이디 -- GET -- 게시글 수정 폼 제공
    // /updateform -- POST -- 게시글 실제 수정 기능 (역시 비밀번호가 일치해야 가능)
    @GetMapping("/updateform")
    public String showupdateForm(@RequestParam int id, Model model){
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);

        return "update";
    }

    @PostMapping("/updateform")
    public String updateBoard(@ModelAttribute("board") Board board, RedirectAttributes redirectAttributes){

        // update.html에서 id를 hidden으로 설정해 mapping한 이유 -- board.getId() 하기 위해..
        String realPassword = boardService.findBoardById(board.getId()).getPassword();

        if (realPassword.equals(board.getPassword())){ // 사용자가 입력한 암호와, 해당 암호가 일치한다면, update 가능

            // 수정할 때, /writeform url에서와 같이 LocalDateTime 추가해줘야, 최근 게시글이 가장 먼저 뜬다.
            LocalDateTime now = LocalDateTime.now();
            board.setCreated_at(now);
            board.setUpdated_at(now);

            boardService.saveBoard(board);
            return "redirect:/list";
        } else {
            // 비밀번호가 틀렸다! 다시 비밀번호 입력해주세요.. + 에러메시지 전달
            redirectAttributes.addAttribute("errorMsg", true);
            return "redirect:/updateform?id=" + board.getId();
        }
    }

    // 좋아요 기능 추가
    @PostMapping("/like")
    public String likeBoard(@RequestParam("id") int id){
        //System.out.println("id : " + id);
        boardService.incrementLikes(id);
        return "redirect:/view?id=" + id;
    }
}
