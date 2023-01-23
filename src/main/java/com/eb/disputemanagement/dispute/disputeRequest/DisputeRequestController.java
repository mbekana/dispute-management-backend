package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import com.eb.disputemanagement.dispute.notification.NotificationDto;
import com.eb.disputemanagement.dispute.utils.PaginatedResultsRetrievedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dispute-requests")
@RequiredArgsConstructor
public class DisputeRequestController implements DisputeRequestApi{

    private final DisputeRequestMapper disputeRequestMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final DisputeRequestService disputeRequestService;

    @Override
    public DisputeRequestDto createDisputeRequest(JwtAuthenticationToken token,  @RequestBody @Valid DisputeRequestDto disputeRequestDto) throws IllegalAccessException {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.createDisputeRequest(token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequests(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  disputeRequestService.getDisputeRequests(pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getDisputeRequests(pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public DisputeRequestDto getDisputeRequest(long id) {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.getDisputeRequest(id));
    }

    @Override
    public DisputeRequestDto updateDisputeRequest(long id, @RequestBody DisputeRequestDto disputeRequestDto, JwtAuthenticationToken token) throws  IllegalAccessException{
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.updateDisputeRequest(id, disputeRequestMapper.toDisputeRequest(disputeRequestDto), token));
    }

    @Override
    public void deleteDisputeRequest(long id, JwtAuthenticationToken token) {
        disputeRequestService.deleteDisputeRequest(id, token);
    }

    @Override
    public DisputeRequestDto receiveDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.receiveDisputeRequest(id, token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public DisputeRequestDto acknowledgeRequest(long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.acknowledgeRequest(id, token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestsByMaker(Pageable pageable, PagedResourcesAssembler assembler, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.getByRequestInitiator(token, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getByRequestInitiator(token, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestsByRequestType(Pageable pageable, PagedResourcesAssembler assembler, DRequestType requestType, RequestStatus status, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.getDisputeByRequestTypeAndStatus(requestType, status, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getDisputeByRequestTypeAndStatus(requestType, status, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getAcknowledgedDisputeRequestsByRequestTypeAndStatus(Pageable pageable, DRequestType requestType, RequestStatus status, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.getByAcknowledgedAndRequestTypeAndStatus(true, requestType, status, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getByAcknowledgedAndRequestTypeAndStatus(true, requestType, status, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getAcknowledgedDisputeRequests(Pageable pageable, PagedResourcesAssembler assembler, boolean acknowledged, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.getAcknowledgedDisputeRequests(true, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getAcknowledgedDisputeRequests(true, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestsByRequestInitiatorBranchAndRequestType(Pageable pageable, PagedResourcesAssembler assembler, JwtAuthenticationToken token, DRequestType requestType, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.getByRequestInitiatorBranchAndRequestType(token, requestType, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getByRequestInitiatorBranchAndRequestType(token, requestType, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getLastFiveNewDisputeRequests(Pageable pageable, PagedResourcesAssembler assembler, JwtAuthenticationToken token, boolean newRequest, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.getLastFiveNewDisputeRequests(token, true, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getLastFiveNewDisputeRequests(token, true,  pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestByBranchCode(Pageable pageable, PagedResourcesAssembler assembler, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  disputeRequestService.getByBranchCode(token, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getByBranchCode(token, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getDisputeRequestByPayerBranch(Pageable pageable, PagedResourcesAssembler assembler, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  disputeRequestService.getByPayerBranch(token, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getByPayerBranch(token, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getExpiringDisputeRequests(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
          LocalDate today =  LocalDate.now();
          LocalDate due = today.minusDays(6);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  disputeRequestService.getExpiringRequests(true, DRequestType.ENAT_ON_OTHER_BANK, RequestStatus.PROCESSING, due, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getExpiringRequests(true, DRequestType.ENAT_ON_OTHER_BANK, RequestStatus.PROCESSING, due, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public DisputeRequestDto markAsNotFound(long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.markAsNotFound(id, token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public DisputeRequestDto approveDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.approveRequest(id, token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public DisputeRequestDto settleDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.settleRequest(id, token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public DisputeRequestDto declineDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequestDto disputeRequestDto) throws IllegalAccessException {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.declineDisputeRequest(id, token, disputeRequestMapper.toDisputeRequest(disputeRequestDto)));
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> searchDisputeRequest(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, Specification<DisputeRequest> search, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), disputeRequestService.searchDisputeRequest(search,pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.searchDisputeRequest(search,pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<DisputeRequestDto>> getFirst5ByNewRequest(Pageable pageable, PagedResourcesAssembler assembler, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                NotificationDto.class, uriBuilder, response, pageable.getPageNumber(),  disputeRequestService.getFirst5ByNewRequest(true, pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<DisputeRequestDto>>(assembler.toModel(disputeRequestService.getFirst5ByNewRequest(true, pageable).map(disputeRequestMapper::toDisputeRequestDto)), HttpStatus.OK);
    }

    @Override
    public DisputeRequestDto confirmSettlemt(long id, JwtAuthenticationToken token) {
        return disputeRequestMapper.toDisputeRequestDto(disputeRequestService.confirmSettlement(id, token));
    }


}
