package com.eb.disputemanagement.dispute.banks;

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

public interface BankApi {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    BankDto createBank(@RequestBody @Valid BankDto bank) throws IllegalAccessException;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BankDto getBank(@PathVariable("id") long id);

    @PutMapping("/{id}")
    BankDto updateBank(@PathVariable("id") long id, @RequestBody @Valid BankDto bank);


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<BankDto>> getBanks(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                 @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder

            , final HttpServletResponse response);


    @DeleteMapping("/{id}")
    void deleteBank(@PathVariable("id") long id, JwtAuthenticationToken token);

    void existsBySwiftCode(String swiftCode);


    @GetMapping("/search")
    ResponseEntity<PagedModel<BankDto>> searchBank(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                       @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder
            , @SearchSpec Specification<Bank> search
            , final HttpServletResponse response);
}
