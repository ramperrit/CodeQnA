package com.codeqna.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ParentReplyDto {
    private Long bno;
    private String nickname;
    private String content;
}
