package com.codeqna.service;

import com.codeqna.dto.AddBoardRequest;
import com.codeqna.dto.BoardViewDto;
import com.codeqna.entity.Board;
import com.codeqna.entity.Uploadfile;
import com.codeqna.repository.BoardRepository;
import com.codeqna.repository.UploadfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UploadfileRepository uploadfileRepository;

    // 게시물 전체 리스트를 가져오기
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // 검색조건에 맞는 게시물을 가져오기
    public List<Board> searchBoards(String condition, String keyword) {
        if (condition.equals("title")) {
            return boardRepository.findByTitleContaining(keyword);
        } else if (condition.equals("content")) {
            return boardRepository.findByContentContaining(keyword);
        } else if (condition.equals("nickname")) {
            return boardRepository.findByNicknameContaining(keyword);
        } else if (condition.equals("hashtag")) {
            String[] keywords = Arrays.stream(keyword.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())  // 공백만 있는 경우 필터링
                    .toArray(String[]::new);
            return boardRepository.findByHashtagsContaining(keywords);
        } else {
            // 검색 조건이 잘못된 경우 처리
            throw new IllegalArgumentException("Invalid search condition: " + condition);
        }
    }

    //게시물 상세 페이지 가져오기
    public Board findByBno(Long bno) {
        return boardRepository.findByBno(bno);
    }

    public Board save(AddBoardRequest request){
        // Board 저장
        Board savedBoard = boardRepository.save(request.toEntity());

        // Uploadfile 엔티티 생성 및 저장
        List<Uploadfile> uploadFiles = request.getFiles().stream()
                .map(fileDto -> {
                    Uploadfile uploadfile = new Uploadfile();
                    uploadfile.setBoard(savedBoard);
                    uploadfile.setOriginal_file_name(fileDto.getOriginalFileName());
                    uploadfile.setSaved_file_name(fileDto.getSavedFileName());
                    return uploadfile;
                })
                .collect(Collectors.toList());

        uploadfileRepository.saveAll(uploadFiles);

        return savedBoard;
    }

    //게시물 삭제
    @Transactional //이게 왜 붙어야하는지 모르겠음
    public void deleteBoard(Long bno) {
        System.out.println("요까지2");
        Board board = boardRepository.findByBno(bno);

        board.deleteBoard();
    }

    //게시물 좋아요
    @Transactional
    public void increaseHeart(Long bno) {
        Board board = boardRepository.findByBno(bno);

        board.increaseHeart();
    }

    //게시물 좋아요 취소
    @Transactional
    public void decreaseHeart(Long bno) {
        System.out.println("좋아요 취소");
        Board board = boardRepository.findByBno(bno);

        board.decreaseHeart();
    }

}

