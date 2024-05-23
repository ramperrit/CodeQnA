package com.codeqna.repository;

import com.codeqna.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query(value = "SELECT * FROM reply WHERE bno = ?1 AND reply_condition LIKE ?2", nativeQuery = true)
    List<Reply> findByBnoAndReply_condition(Long bno, String condition);
    List<Reply> findByBoard_Bno(Long articleId);
    void deleteByIdAndUser_Email(Long articleCommentId, String email);

    void deleteByUser_Email(String email);


    Optional<Reply> findById(Long parentCommentId);
}
