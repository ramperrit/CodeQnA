package com.codeqna.repository;

import com.codeqna.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Board findByBno(Long bno);

    // 칼럼에 키워드값이 포함되어 있는 보드리스트
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:title% AND b.board_condition = :boardCondition")
    List<Board> findByTitleContaining(@Param("title") String keyword, @Param("boardCondition") String boardCondition);
    @Query("SELECT b FROM Board b WHERE b.content LIKE %:content% AND b.board_condition = :boardCondition")
    List<Board> findByContentContaining(@Param("content") String keyword, @Param("boardCondition") String boardCondition);
    @Query("SELECT b FROM Board b WHERE b.nickname LIKE %:nickname% AND b.board_condition = :boardCondition")
    List<Board> findByNicknameContaining(@Param("nickname") String keyword, @Param("boardCondition") String boardCondition);

    @Transactional
    @Modifying
    @Query("update Board set hitcount = hitcount + 1 where bno = :bno")
    void incrementHitCount(@Param("bno") Long bno);
}
