package com.codeqna.service;

import com.codeqna.dto.HeartDto;
import com.codeqna.entity.Board;
import com.codeqna.entity.Heart;
import com.codeqna.repository.BoardRepository;
import com.codeqna.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final BoardRepository boardRepository;
    private final HeartRepository heartRepository;

    //-------------------------------------------------------------
    //게시물 좋아요
    @Transactional
    public void increaseHeart(HeartDto heartDto) {
        Board board = boardRepository.findByBno(heartDto.getBno());
        //System.out.println("야임마! : " + heartDto.getNickname());
        board.increaseHeart();
        insertHeartUser(heartDto);
    }

    // 좋아요 클릭 시 heart 테이블에 nickname과 bno 저장
    public void insertHeartUser(HeartDto heartDto) {
        Heart heart = new Heart();
        heart.setNickname(heartDto.getNickname());

        Board board = boardRepository.findByBno(heartDto.getBno());
        heart.setBoard(board);
        heartRepository.save(heart);
    }
    //-------------------------------------------------------------

    //-------------------------------------------------------------
    //게시물 좋아요 취소
    @Transactional
    public void decreaseHeart(HeartDto heartDto) {
        System.out.println("좋아요 취소");
        Board board = boardRepository.findByBno(heartDto.getBno());

        board.decreaseHeart();
        deleteHeartUser(heartDto.getBno(), heartDto.getNickname());
    }

    public void deleteHeartUser(Long bno, String nickname) {
        heartRepository.deleteByNickname(bno, nickname);
    }
    //-------------------------------------------------------------

    //게시물 heart 눌렀는지 여부
    public Heart isHeart(HeartDto heartDto) {
        return heartRepository.isHeart(heartDto.getBno(), heartDto.getNickname());
    }

}
