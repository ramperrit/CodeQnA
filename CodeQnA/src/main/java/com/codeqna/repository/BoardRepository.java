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
    List<Board> findByTitleContaining(String keyword);
    List<Board> findByContentContaining(String keyword);
    List<Board> findByNicknameContaining(String keyword);

    @Transactional
    @Modifying
    @Query("update Board set hitcount = hitcount + 1 where bno = :bno")
    void incrementHitCount(@Param("bno") Long bno);
}
