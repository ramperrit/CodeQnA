package com.codeqna.service;


import com.codeqna.dto.ArticleCommentDto;
import com.codeqna.dto.request.ArticleCommentRequest;
import com.codeqna.dto.response.ArticleCommentResponse;
import com.codeqna.entity.Board;
import com.codeqna.entity.Reply;
import com.codeqna.entity.Users;
import com.codeqna.repository.BoardRepository;
import com.codeqna.repository.ReplyRepository;
import com.codeqna.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;


    public Set<ArticleCommentResponse> searchArticleComments(Long articleId) {

        Map<Long, ArticleCommentResponse> map =  replyRepository.findByBoard_Bno(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toMap(ArticleCommentResponse::getId, Function.identity()));

        map.values().stream()
                .filter(ArticleCommentResponse::hasParentComment)
                .forEach(comment -> {
                    ArticleCommentResponse parentComment = map.get(comment.getParentCommentId());
                    parentComment.getChildComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(ArticleCommentResponse::getCreatedAt)
                                .reversed()
                                .thenComparingLong(ArticleCommentResponse::getId)
                        )
                ));



    }

    public void saveArticleComment( ArticleCommentRequest articleCommentRequest, String email) {
        try {


            Board board = boardRepository.findByBno(articleCommentRequest.getArticleId());
            Users user = userRepository.findByEmail(email).orElseThrow();
            Reply reply = Reply.of(board,user,articleCommentRequest.getContent());

            if (articleCommentRequest.getParentCommentId() != null) {
                Reply parentComment = replyRepository.findById(articleCommentRequest.getParentCommentId()).orElseThrow();
                parentComment.addChildComment(reply);
            } else {
                replyRepository.save(reply);
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void deleteArticleComment(Long articleCommentId, String email) {
        replyRepository.deleteByIdAndUser_Email(articleCommentId, email);

    }

    public void updateArticleComment(Long articleCommentId, ArticleCommentRequest articleCommentRequest, String email) {
        try {
            //article, useraccount 찾아옴
            Reply reply = replyRepository.getReferenceById(articleCommentId);
            Users user = userRepository.findByEmail(email).orElseThrow();
            //게시글 엔티티에서 꺼내온 useraccount 엔티티가, 수정하려는 작성자 useraccount 엔티티와 같으면
            if (reply.getUser().getEmail().equals(user.getEmail())) {
                //dto의 title,content가 null이 아니면 set

                if (articleCommentRequest.getContent() != null) { reply.setContent(articleCommentRequest.getContent()); }

            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }



}
