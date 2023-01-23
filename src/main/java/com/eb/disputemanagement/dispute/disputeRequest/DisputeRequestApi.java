package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
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

public interface DisputeRequestApi {
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    DisputeRequestDto createDisputeRequest(JwtAuthenticationToken token, @RequestBody @Valid DisputeRequestDto disputeRequestDto) throws IllegalAccessException;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequests(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                     @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder

            , final HttpServletResponse response);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DisputeRequestDto getDisputeRequest(@PathVariable("id") long id);

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto updateDisputeRequest(@PathVariable("id") long id, @RequestBody @Valid DisputeRequestDto disputeRequestDto, JwtAuthenticationToken token) throws IllegalAccessException;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDisputeRequest(@PathVariable("id") long id, JwtAuthenticationToken token);

    @PutMapping("/receive/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto receiveDisputeRequest(@PathVariable("id") long id, JwtAuthenticationToken token, @RequestBody  DisputeRequestDto disputeRequestDto) throws IllegalAccessException;

    @PutMapping("/acknowledge/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto acknowledgeRequest(@PathVariable("id") long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException;

    @GetMapping("/maker")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestsByMaker(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
            @Valid Pageable pageable,
            PagedResourcesAssembler assembler,
            JwtAuthenticationToken token,
            UriComponentsBuilder uriBuilder,
            final HttpServletResponse response);

    @GetMapping("/requestType")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestsByRequestType(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                            @Valid Pageable pageable,
                                                                            PagedResourcesAssembler assembler,
                                                                            DRequestType requestType,
                                                                            RequestStatus status,
                                                                            UriComponentsBuilder uriBuilder,
                                                                            final HttpServletResponse response);

    @GetMapping("/acknowledged")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getAcknowledgedDisputeRequestsByRequestTypeAndStatus(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                  @Valid Pageable pageable,
                                                                                  DRequestType requestType,
                                                                                  RequestStatus status,
                                                                                  PagedResourcesAssembler assembler,
                                                                                  UriComponentsBuilder uriBuilder,
                                                                                  final HttpServletResponse response);

    @GetMapping("/acknowledged/no-stat")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getAcknowledgedDisputeRequests(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                                       @Valid Pageable pageable,
                                                                                                       PagedResourcesAssembler assembler,
                                                                                                       boolean acknowledged,
                                                                                                       UriComponentsBuilder uriBuilder,
                                                                                                       final HttpServletResponse response);

    @GetMapping("/branch-and-request-type")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestsByRequestInitiatorBranchAndRequestType(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                  @Valid Pageable pageable,
                                                                                  PagedResourcesAssembler assembler,
                                                                                  JwtAuthenticationToken token,
                                                                                  DRequestType requestType,
                                                                                  UriComponentsBuilder uriBuilder,
                                                                                  final HttpServletResponse response);

    @GetMapping("/last-five-new requests-by-branch")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getLastFiveNewDisputeRequests(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                                           @Valid Pageable pageable,
                                                                                                           PagedResourcesAssembler assembler,
                                                                                                           JwtAuthenticationToken token,
                                                                                                           boolean newRequest,
                                                                                                           UriComponentsBuilder uriBuilder,
                                                                                                           final HttpServletResponse response);

    @GetMapping("/branch-code")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestByBranchCode(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                @Valid Pageable pageable,
                                                                                PagedResourcesAssembler assembler,
                                                                                JwtAuthenticationToken token,
                                                                                UriComponentsBuilder uriBuilder,
                                                                                final HttpServletResponse response);

    @GetMapping("/payer-branch")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestByPayerBranch(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                @Valid Pageable pageable,
                                                                                PagedResourcesAssembler assembler,
                                                                                JwtAuthenticationToken token,
                                                                                UriComponentsBuilder uriBuilder,
                                                                                final HttpServletResponse response);
    @GetMapping("/expiring")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PagedModel<DisputeRequestDto>> getExpiringDisputeRequests(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                                 @Valid Pageable pageable,
                                                                             PagedResourcesAssembler assembler,
                                                                             UriComponentsBuilder uriBuilder,
                                                                             final HttpServletResponse response);
    @PutMapping("/not-found/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto markAsNotFound(@PathVariable("id")long id, JwtAuthenticationToken token,  DisputeRequestDto disputeRequestDto);

    @PutMapping("/approve/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto approveDisputeRequest(@PathVariable("id")long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException;

    @PutMapping("/settle/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto settleDisputeRequest(@PathVariable("id") long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException;

    @PutMapping("/decline/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    DisputeRequestDto declineDisputeRequest(@PathVariable("id")long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException;


    @GetMapping("/search")
    ResponseEntity<PagedModel<DisputeRequestDto>> searchDisputeRequest(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                  @Valid Pageable pageable
            , PagedResourcesAssembler assembler
            , UriComponentsBuilder uriBuilder
            , @SearchSpec Specification<DisputeRequest> search
            , final HttpServletResponse response);

    @GetMapping("/last-five-from-all-branches")
    ResponseEntity<PagedModel<DisputeRequestDto>> getFirst5ByNewRequest(@Parameter(description = "pagination object",
            schema = @Schema(implementation = Pageable.class))
                                                                        @Valid Pageable pageable,
                                                                        PagedResourcesAssembler assembler,
                                                                        JwtAuthenticationToken token,
                                                                        UriComponentsBuilder uriBuilder,
                                                                        final HttpServletResponse response);

    @PutMapping("/confirm-settlement/{id}")
    DisputeRequestDto confirmSettlemt(@PathVariable("id")long id, JwtAuthenticationToken token);

}
