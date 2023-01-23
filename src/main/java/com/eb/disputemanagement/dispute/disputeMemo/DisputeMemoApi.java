package com.eb.disputemanagement.dispute.disputeMemo;

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


public interface DisputeMemoApi {

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    DisputeMemoDto createDisputeMemo(@PathVariable("id") long id, JwtAuthenticationToken token, @RequestBody @Valid DisputeMemoDto disputeMemoDto) throws IllegalAccessException;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DisputeMemoDto getDisputeMemo(@PathVariable("id") long id);

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeMemoDto>> getDisputeMemos(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
              @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder

            , final HttpServletResponse response);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeMemoDto updateDisputeMemo(@PathVariable("id") long id, @RequestBody @Valid DisputeMemoDto disputeMemoDto, JwtAuthenticationToken token) throws IllegalAccessException;

    @DeleteMapping("/{id}")
    void deleteDisputeMemo(@PathVariable("id") long id);
}
