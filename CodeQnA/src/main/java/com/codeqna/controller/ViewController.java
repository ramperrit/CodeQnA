package com.codeqna.controller;


import com.codeqna.dto.BoardViewDto;
import com.codeqna.dto.UserFormDto;
import com.codeqna.entity.Board;
import com.codeqna.entity.Reply;
import com.codeqna.entity.Users;
import com.codeqna.repository.BoardRepository;
import com.codeqna.repository.UserRepository;
import com.codeqna.service.BoardService;
import com.codeqna.service.ReplyService;
import com.codeqna.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserRepository userRepository;
    private final BoardService boardService;
    private final BoardRepository repository;
    private final ReplyService replyService;

    @GetMapping("/main")
    public String boardList(Model model) {
        // 게시글 목록을 가져와서 모델에 추가
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "boardlist"; // HTML 템플릿 이름 리턴
    }

    @GetMapping("/Loginmain")
    public String Loginmainpage(Model model){
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users users = userRepository.findByEmail(email);
        model.addAttribute("nickname", users.getNickname());
        return "boardlist";
    }

    @GetMapping("/admin/deleted")
    public String deletedBoard(){
        return "admin/deletedBoards";
    }

    @GetMapping("/admin/boards")
    public String manageBoards(){
        return "admin/manageBoards";
    }

    @GetMapping("/admin/comments")
    public String manageComments(){return "admin/manageComments";}

    @GetMapping("/admin/users")
    public String manageUsers(){
        return "admin/manageUsers";
    }

    @GetMapping("/admin/files")
    public String manageFiles(){
        return "admin/manageFiles";
    }

    @GetMapping("/newboard")
    public String newboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users users = userRepository.findByEmail(email);
        model.addAttribute("nickname", users.getNickname());

        model.addAttribute("board", new BoardViewDto());

        return "newboard";
    }

    @GetMapping("/modifyboard")
    public String modifyboard(@RequestParam(required = false) Long bno, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users users = userRepository.findByEmail(email);
        model.addAttribute("nickname", users.getNickname());

        //지금은 board 데이터 전부 다 넘겨주고 있는데
        //나중에 제목, 내용, 해시태그, 첨부파일 이것만 보내주면 댐
        Board board = boardService.findByBno(bno);
        model.addAttribute("board", board);
        return "modifyboard";
    }

    @GetMapping("/viewboard/{bno}")
    public String viewBoard(@PathVariable Long bno, Model model) {

        Board board = boardService.findByBno(bno);
        List<Reply> reply = replyService.findByBno(bno);

        //없는 게시물에 들어갔을 때
        if(board == null){
            return "error2";
        }

        //삭제된 게시물에 접근할 때
        if(board.getBoard_condition().equals("Y")) {
            return "error2";
        }
        /*System.out.println(board.getTitle());
        System.out.println(board.getUser_id());*/

        //정상적인 페이지로 들어갔을 경우 조회수 + 1
        repository.incrementHitCount(bno);

        String hashtags = board.getHashtag();
        List<String> hashtagList = Arrays.asList(hashtags.split("#"));
        // 빈 문자열을 제외합니다.
        hashtagList = hashtagList.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        model.addAttribute("hashtags", hashtagList);

        //로그인된 사용자의 nickname을 넘김, 없으면 오류, 빈 값을 넘김
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users users = userRepository.findByEmail(email);
        if(users != null) {
            model.addAttribute("nickname", users.getNickname());
        } else {
            model.addAttribute("nickname", "");
        }

        model.addAttribute("board", board);

        model.addAttribute("reply", reply);
        return "viewBoard";
    }
}
