package com.codeqna.dto;


import com.codeqna.entity.Board;
import com.codeqna.entity.Reply;
import com.codeqna.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleCommentDto {
    private Long id;
    private Long articleId;
    private UserDto userDto;
    private Long parentCommentId;
    private String content;
    private LocalDateTime regdate;


    public static ArticleCommentDto of(Long articleId, UserDto memberDto, String content) {
        return ArticleCommentDto.of(articleId, memberDto, null, content);
    }

    public static ArticleCommentDto of(Long articleId, UserDto memberDto, Long parentCommentId, String content) {
        return ArticleCommentDto.of(null, articleId, memberDto, parentCommentId, content, null);
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserDto memberDto, Long parentCommentId, String content, LocalDateTime createdAt) {
        return new ArticleCommentDto(id, articleId, memberDto, parentCommentId, content, createdAt);
    }

    public static ArticleCommentDto from(Reply entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getBoard().getBno(),
                UserDto.from(entity.getUser()),
                entity.getParentCommentId(),
                entity.getContent(),
                entity.getRegdate()

        );
    }

    public Reply toEntity(Board board, Users user) {
        return Reply.of(
                board,
                user,
                content
        );
    }
}
