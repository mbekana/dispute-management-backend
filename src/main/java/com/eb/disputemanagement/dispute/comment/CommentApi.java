package com.eb.disputemanagement.dispute.comment;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface CommentApi {

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto createComment(@PathVariable("id") long id, JwtAuthenticationToken token, @RequestBody @Valid CommentDto commentDto);

    @GetMapping("/{id}/comments")
    ResponseEntity<PagedModel<CommentDto>> getComments(@PathVariable("id") long id,@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class)) @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder
            , final HttpServletResponse response);

    @PutMapping("comments/{id}")
    CommentDto updateComment(@PathVariable("id") long id, JwtAuthenticationToken token, CommentDto commentDto);

    @GetMapping("/{id}/comment")
    CommentDto getComment(@PathVariable("id") long id);

    @DeleteMapping("comments/{id}")
    void deleteComment(long id);

    @PutMapping("/comment/{requestId}")
    void readComment(@PathVariable("requestId") long requestId, JwtAuthenticationToken token);
}
