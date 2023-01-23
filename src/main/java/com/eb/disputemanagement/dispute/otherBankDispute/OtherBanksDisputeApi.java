package com.eb.disputemanagement.dispute.otherBankDispute;

import com.sipios.springsearch.anotation.SearchSpec;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface OtherBanksDisputeApi {


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    OtherBanksDisputeDto createOtherBanksDispute(JwtAuthenticationToken token, @RequestBody @Valid OtherBanksDisputeDto otherBanksDisputeDto);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    OtherBanksDisputeDto getOtherBanksDispute(@PathVariable("id") long id);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    OtherBanksDisputeDto updateOtherBanksDispute(@PathVariable("id") long id, JwtAuthenticationToken token, @RequestBody @Valid OtherBanksDisputeDto otherBanksDisputeDto) throws IllegalAccessException;

    @GetMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<PagedModel<OtherBanksDisputeDto>> getOtherBanksDisputes(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                 @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder

            , final HttpServletResponse response);


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteOtherBanksDispute(@PathVariable("id") long id);


    @GetMapping("/search")
    ResponseEntity<PagedModel<OtherBanksDisputeDto>> searchOtherBankDispute(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                       @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder
            , @SearchSpec Specification<OtherBanksDispute> search
            , final HttpServletResponse response);
}
