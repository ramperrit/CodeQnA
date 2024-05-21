package com.codeqna.controller;

import com.codeqna.dto.AddBoardRequest;
import com.codeqna.dto.HeartDto;
import com.codeqna.dto.ModifyBoardRequest;
import com.codeqna.dto.ParentReplyDto;
import com.codeqna.entity.Board;
import com.codeqna.entity.Heart;
import com.codeqna.service.BoardService;
import com.codeqna.service.HeartService;
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
    private final HeartService heartService;

    // 게시물 등록
    @PostMapping("/register")
    public ResponseEntity<Board> addBoard(@RequestBody AddBoardRequest request){
        Board savedBoard = boardService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoard);
    }

    // 게시물 검색
    @GetMapping("/searchTable")
    public List<Board> searchBoards(@RequestParam("condition") String condition,
                                    @RequestParam("keyword") String keyword) {
        return boardService.searchBoards(condition, keyword);
    }

    // 게시물 수정
    @PostMapping("/modify")
    public ResponseEntity<Board> modifyBoard(@RequestBody ModifyBoardRequest request){
        Board modifiedBoard = boardService.modify(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(modifiedBoard);
    }


    //게시물 삭제
    @PutMapping("/delete/{bno}")
    public ResponseEntity<Void> delete(@PathVariable Long bno) {
        boardService.deleteBoard(bno);
        System.out.println("요까지1");

        return ResponseEntity.ok().build();
    }

    //좋아요를 눌렀을 경우
    @PutMapping("/heart/like")
    public ResponseEntity<Void> increaseHeart(@RequestBody HeartDto heartDto) {
        heartService.increaseHeart(heartDto);
        //System.out.println("닉네임 오냐? : " + heartDto.getNickname());

        return ResponseEntity.ok().build();

    }

    //좋아요를 취소했을 경우
    @PutMapping("/heart/unlike")
    public ResponseEntity<Void> decreaseHeart(@RequestBody HeartDto heartDto) {
        heartService.decreaseHeart(heartDto);
        return ResponseEntity.ok().build();
    }

    //로그인 후 상세 페이지에 들어갔을 때 그 사용자가 그 게시물에 좋아요를 눌렀는지 여부 반환
    @PostMapping("/heart/pressHeart")
    public @ResponseBody Heart isHeart(@RequestBody HeartDto heartDto) {
        System.out.println("이게 실행되나?");
        System.out.println("이게 json : " + heartService.isHeart(heartDto));
        return heartService.isHeart(heartDto);
    }

    //댓글 등록
    @PostMapping("/comment/add")
    public ResponseEntity<ParentReplyDto> addComment(@RequestBody ParentReplyDto parentReplyDto) {
        replyService.addComment(parentReplyDto);
        return ResponseEntity.ok().build();
    }

}
