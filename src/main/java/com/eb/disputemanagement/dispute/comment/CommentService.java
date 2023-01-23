package com.eb.disputemanagement.dispute.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment createComment(long id, JwtAuthenticationToken token, Comment comment);
    Page<Comment> getComments(long id, Pageable pageable);
    Comment updateComment(long id, JwtAuthenticationToken token, Comment comment);
    Comment getComment(long id);
    void deleteComment(long id);
    void readComment(long id, JwtAuthenticationToken token);
}
