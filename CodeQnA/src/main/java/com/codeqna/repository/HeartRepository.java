package com.codeqna.repository;

import com.codeqna.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Modifying
    @Query(value = "delete from heart where bno = :bno AND nickname = :nickname", nativeQuery = true)
    void deleteByNickname(Long bno, String nickname);

    @Query(value = "SELECT * FROM heart WHERE bno = :bno AND nickname = :nickname", nativeQuery = true)
    Heart isHeart(Long bno, String nickname);
}
