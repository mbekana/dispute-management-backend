package com.eb.disputemanagement.dispute.comment;

import com.eb.disputemanagement.dispute.utils.PaginatedResultsRetrievedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@Service
@RequestMapping("/api/v1/dispute-requests")
@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi{

    private final CommentMapper commentMapper;

    private final CommentService commentService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CommentDto createComment(long id, JwtAuthenticationToken token, CommentDto commentDto) {
        return commentMapper.toCommentDto(commentService.createComment(id,token, commentMapper.toComment(commentDto)));
    }

    @Override
    public ResponseEntity<PagedModel<CommentDto>> getComments(long id, Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                CommentDto.class, uriBuilder, response, pageable.getPageNumber(),  commentService.getComments(id, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<CommentDto>>(assembler.toModel(commentService.getComments(id, pageable).map(commentMapper::toCommentDto)), HttpStatus.OK);
    }

    @Override
    public CommentDto updateComment(long id, JwtAuthenticationToken token, CommentDto commentDto) {
        return commentMapper.toCommentDto(commentService.updateComment(id, token, commentMapper.toComment(commentDto)));
    }

    @Override
    public CommentDto getComment(long id) {
        return commentMapper.toCommentDto(commentService.getComment(id));
    }

    @Override
    public void deleteComment(long id) {
        commentService.deleteComment(id);
    }

    @Override
    public void readComment(long commentId, JwtAuthenticationToken token) {
        commentService.readComment(commentId,  token);
    }
}
