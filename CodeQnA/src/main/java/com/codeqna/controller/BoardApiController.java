package com.codeqna.controller;

import com.codeqna.dto.AddBoardRequest;
import com.codeqna.dto.ParentReplyDto;
import com.codeqna.entity.Board;
import com.codeqna.service.BoardService;
import com.codeqna.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardAPI")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;
    private final ReplyService replyService;

    @PostMapping("/register")
    public ResponseEntity<Board> addBoard(@RequestBody AddBoardRequest request){
        Board savedBoard = boardService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }

    @GetMapping("/searchTable")
    public List<Board> searchBoards(@RequestParam("condition") String condition,
                                    @RequestParam("keyword") String keyword) {
        return boardService.searchBoards(condition, keyword);
    }


    //게시물 삭제
    @PutMapping("/delete/{bno}")
    public ResponseEntity<Void> delete(@PathVariable Long bno) {
        boardService.deleteBoard(bno);
        System.out.println("요까지1");

        return ResponseEntity.ok().build();
    }

    //좋아요를 눌렀을 경우
    @PutMapping("/heart/{bno}/like")
    public ResponseEntity<Void> increaseHeart(@PathVariable Long bno) {
        boardService.increaseHeart(bno);
        return ResponseEntity.ok().build();
    }

    //좋아요를 취소했을 경우
    @PutMapping("/heart/{bno}/unlike")
    public ResponseEntity<Void> decreaseHeart(@PathVariable Long bno) {
        boardService.decreaseHeart(bno);
        return ResponseEntity.ok().build();
    }

    //댓글 등록
    @PostMapping("/comment/add")
    public ResponseEntity<ParentReplyDto> addComment(@RequestBody ParentReplyDto parentReplyDto) {
        replyService.addComment(parentReplyDto);
        return ResponseEntity.ok().build();
    }

}
