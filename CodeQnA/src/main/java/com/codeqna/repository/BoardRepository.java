package com.codeqna.repository;

import com.codeqna.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByBno(Long bno);
}
