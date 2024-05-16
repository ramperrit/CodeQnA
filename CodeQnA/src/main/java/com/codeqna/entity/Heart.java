package com.codeqna.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hno", nullable = false)
    private Long hno;

    @Column(name = "nickname", nullable = false)
    private Long nickname;

    @ManyToOne
    @JoinColumn(name = "bno", nullable = false)
    private Board board;
}
