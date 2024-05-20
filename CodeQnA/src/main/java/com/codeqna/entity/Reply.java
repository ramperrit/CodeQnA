package com.codeqna.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rno", nullable = false)
    private Long rno;

    @ManyToOne
    @JoinColumn(name = "bno", nullable = false)
    private Board board;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "regdate")
    private LocalDateTime regdate;

    @Column(name = "parent_id")
    private Long parent_id;

    @Column(name = "reply_condition", nullable = false)
    private String reply_condition;

}
