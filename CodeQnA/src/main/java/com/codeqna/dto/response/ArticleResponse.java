package com.codeqna.dto.response;

import com.codeqna.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ArticleResponse {

    private Long bno;
    private String title;
    private String content;
    private String hashtag;
    private Long hitcount;
    private Long heart;
    private LocalDateTime regdate;
    private String nickname;
    private String email;
    private String board_condition;



    public static ArticleResponse from(Board board) {


        return new ArticleResponse(
              board.getBno(),
                board.getTitle(),
                board.getContent(),
                board.getHashtag(),
                board.getHitcount(),
                board.getHeart(),
                board.getRegdate(),
                board.getUser().getNickname(),
                board.getUser().getEmail(),
                board.getBoard_condition()

        );
        //dto의 id, title,content, 해시태그set, 생성시각, email,nickname
    }


}
