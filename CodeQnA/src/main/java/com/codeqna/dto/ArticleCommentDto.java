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
    private String reply_condition;


    public static ArticleCommentDto of(Long articleId, UserDto memberDto, String content,String reply_condition) {
        return ArticleCommentDto.of(articleId, memberDto, null, content,reply_condition);
    }

    public static ArticleCommentDto of(Long articleId, UserDto memberDto, Long parentCommentId, String content,String reply_condition) {
        return ArticleCommentDto.of(null, articleId, memberDto, parentCommentId, content, null,reply_condition);
    }

    public static ArticleCommentDto of(Long id, Long articleId, UserDto memberDto, Long parentCommentId, String content, LocalDateTime createdAt,String reply_condition) {
        return new ArticleCommentDto(id, articleId, memberDto, parentCommentId, content, createdAt,reply_condition);
    }

    public static ArticleCommentDto from(Reply entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getBoard().getBno(),
                UserDto.from(entity.getUser()),
                entity.getParentCommentId(),
                entity.getContent(),
                entity.getRegdate(),
                entity.getReply_condition()

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
