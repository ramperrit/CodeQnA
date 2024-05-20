package com.codeqna.repository;

import com.codeqna.entity.Uploadfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadfileRepository extends JpaRepository<Uploadfile, Long> {
    // 여기에 필요한 추가적인 메서드 선언이 가능합니다.
}
