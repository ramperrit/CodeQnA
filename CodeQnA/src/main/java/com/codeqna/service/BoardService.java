package com.codeqna.service;

import com.codeqna.dto.AddBoardRequest;
import com.codeqna.dto.BoardViewDto;
import com.codeqna.dto.LogsViewDto;
import com.codeqna.dto.ModifyBoardRequest;
import com.codeqna.entity.Board;
import com.codeqna.entity.Logs;
import com.codeqna.entity.Uploadfile;
import com.codeqna.repository.BoardRepository;
import com.codeqna.repository.LogsRepository;
import com.codeqna.repository.UploadfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UploadfileRepository uploadfileRepository;
    private final LogsRepository logsRepository;

    // 게시물 전체 리스트를 가져오기
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // 검색조건에 맞는 게시물을 가져오기
    public List<Board> searchBoards(String condition, String keyword) {
        if (condition.equals("title")) {
            return boardRepository.findByTitleContaining(keyword, "N");
        } else if (condition.equals("content")) {
            return boardRepository.findByContentContaining(keyword, "N");
        } else if (condition.equals("nickname")) {
            return boardRepository.findByNicknameContaining(keyword, "N");
        } else if (condition.equals("hashtag")) {
            String[] keywords = Arrays.stream(keyword.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())  // 공백만 있는 경우 필터링
                    .toArray(String[]::new);
            return boardRepository.findByHashtagsContaining(keywords, "N");
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

    public Board modify(ModifyBoardRequest request){
        // 기존 게시물을 가져옴
        Board existingBoard = boardRepository.findById(request.getBno()).orElse(null);
        if (existingBoard == null) {
            // 기존 게시물이 존재하지 않는 경우 예외 처리 또는 새로운 게시물로 처리
            throw new EntityNotFoundException("게시물을 찾을 수 없습니다.");
        }

        // 기존 게시물의 정보를 수정
        existingBoard.setTitle(request.getTitle());
        existingBoard.setNickname(request.getNickname());
        existingBoard.setContent(request.getContent());
        existingBoard.setHashtag(request.getHashtag());
        existingBoard.setHeart(request.getHeart());
        existingBoard.setHitcount(request.getHitcount());
        existingBoard.setBoard_condition(request.getBoard_condition());

        // 기타 필요한 속성들도 수정
        // Uploadfile 엔티티 생성 및 저장
        List<Uploadfile> uploadFiles = request.getFiles().stream()
                .map(fileDto -> {
                    Uploadfile uploadfile = new Uploadfile();
                    uploadfile.setBoard(existingBoard);
                    uploadfile.setOriginal_file_name(fileDto.getOriginalFileName());
                    uploadfile.setSaved_file_name(fileDto.getSavedFileName());
                    return uploadfile;
                })
                .collect(Collectors.toList());

        uploadfileRepository.saveAll(uploadFiles);

        // 수정된 게시물을 저장
        Board modifiedBoard = boardRepository.save(existingBoard);

        return modifiedBoard;
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

    // 삭제게시물 가져올때 로그와 같이 가져오는 메서드
    public List<LogsViewDto> getLogWithBoard() {
        List<LogsViewDto> LogsViews = new ArrayList<>();

        List<Logs> logsList = logsRepository.findAll();
        for (Logs logs : logsList) {
            Board board = boardRepository.findByBno(logs.getBoard().getBno());
            if (board != null) {
                LocalDateTime deleteTime = logs.getDelete_time();
                LocalDateTime recoveryTime = logs.getRecover_time();
                LogsViewDto logView = new LogsViewDto(board, deleteTime, recoveryTime);
                LogsViews.add(logView);
            }
        }

        return LogsViews;
    }

    // 삭제게시물에서 날짜검색 게시물 가져오기
    public List<LogsViewDto> searchDateDeleteBoards(String condition, String start, String end) {
        LocalDateTime startDateTime = convertStringToLocalDateTime(start, false);

        if (end == null || end.isEmpty()) {
            if (condition.equals("regdate")) {
                return logsRepository.findLogsByRegdate(startDateTime);
            } else if (condition.equals("deletetime")) {
                return logsRepository.findLogsByDeletetime(startDateTime);
            } else if (condition.equals("recovertime")) {
                return logsRepository.findLogsByRecovertime(startDateTime);
            }
        } else {
            LocalDateTime endDateTime = convertStringToLocalDateTime(end, true);

            // 기존의 날짜 범위 검색 메서드 호출
            if (condition.equals("regdate")) {
                return logsRepository.findLogsByRegdateBetween(startDateTime, endDateTime);
            } else if (condition.equals("deletetime")) {
                return logsRepository.findLogsByDeletetimeBetween(startDateTime, endDateTime);
            } else if (condition.equals("recovertime")) {
                return logsRepository.findLogsByRecovertimeBetween(startDateTime, endDateTime);
            }
        }

        // 검색 조건이 잘못된 경우 처리
        throw new IllegalArgumentException("Invalid search condition: " + condition);
    }

    // 삭제게시물에서 String 검색 게시물을 가져오기
    public List<LogsViewDto> searchStringDeleteBoards(String condition, String keyword) {
        if (condition.equals("title")) {
            return logsRepository.findLogsByTitle(keyword);
        } else if (condition.equals("nickname")) {
            return logsRepository.findLogsByNickname(keyword);
        } else {
            // 검색 조건이 잘못된 경우 처리
            throw new IllegalArgumentException("Invalid search condition: " + condition);
        }
    }

    // 날짜 문자열을 LocalDateTime으로 변환하는 유틸리티 메서드
    private LocalDateTime convertStringToLocalDateTime(String dateStr, boolean isEndOfDay) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        return isEndOfDay ? LocalDateTime.of(localDate, LocalTime.MAX) : LocalDateTime.of(localDate, LocalTime.MIN);
    }

    // 삭제게시물을 복원하는 메서드
    @Transactional
    public void recoverBoards(List<Long> bnos) {
        for (Long bno : bnos) {
            Board board = boardRepository.findByBno(bno);
            if (board != null) {
                // 복원일시 업데이트
                Logs log = logsRepository.findByBoard(board);
                if (log != null) {
                    log.setRecover_time(LocalDateTime.now());
                    logsRepository.save(log);
                }

                // 게시물 상태 업데이트
                board.setBoard_condition("N");
                boardRepository.save(board);
            }
        }
    }

}

