package com.codeqna.repository;

import com.codeqna.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findByIpAddr(String ipAddr);

//    List<Visitor> findByVDate(LocalDate vDate);

    @Query("SELECT v FROM Visitor v WHERE v.vDate = :vDate")
    List<Visitor> findByVDate(@Param("vDate") LocalDate vDate);
}
