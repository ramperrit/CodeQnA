package com.codeqna.repository;

import com.codeqna.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query(value = "SELECT * FROM reply WHERE bno = ?1 AND reply_condition LIKE ?2", nativeQuery = true)
    List<Reply> findByBnoAndReply_condition(Long bno, String condition);
}
