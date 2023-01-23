package com.eb.disputemanagement.dispute.otherBankDispute;

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


@RestController
@RequestMapping("/api/v1/other-banks-dispute")
@RequiredArgsConstructor
public class OtherBanksDisputeController implements  OtherBanksDisputeApi{

    private final OtherBanksDisputeService otherBanksDisputeService;
    private final OtherBanksDisputeMapper otherBanksDisputeMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public OtherBanksDisputeDto createOtherBanksDispute(JwtAuthenticationToken token, @Valid OtherBanksDisputeDto otherBanksDisputeDto) {
        return otherBanksDisputeMapper.toOtherBanksDisputeDto(otherBanksDisputeService.createOtherBanksDispute(token, otherBanksDisputeMapper.toOtherBanksDispute(otherBanksDisputeDto)));
    }

    @Override
    public OtherBanksDisputeDto getOtherBanksDispute(long id) {
        return otherBanksDisputeMapper.toOtherBanksDisputeDto(otherBanksDisputeService.getOtherBanksDispute(id));
    }


    @Override
    public OtherBanksDisputeDto updateOtherBanksDispute(long id, JwtAuthenticationToken token, @RequestBody OtherBanksDisputeDto otherBanksDisputeDto) throws IllegalAccessException {
        return otherBanksDisputeMapper.toOtherBanksDisputeDto(otherBanksDisputeService.updateOtherBanksDispute(id, token,otherBanksDisputeMapper.toOtherBanksDispute(otherBanksDisputeDto)));
    }


    @Override
    public ResponseEntity<PagedModel<OtherBanksDisputeDto>> getOtherBanksDisputes(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                OtherBanksDisputeDto.class, uriBuilder, response, pageable.getPageNumber(),  otherBanksDisputeService.getOtherBanksDisputes(pageable).getTotalPages(), pageable.getPageSize()
        ));
        return new ResponseEntity<PagedModel<OtherBanksDisputeDto>>(assembler.toModel(otherBanksDisputeService.getOtherBanksDisputes(pageable).map(otherBanksDisputeMapper::toOtherBanksDisputeDto)), HttpStatus.OK);
    }

    @Override
    public void deleteOtherBanksDispute(long id) {
        otherBanksDisputeService.deleteOtherBanksDispute(id);
    }

    @Override
    public ResponseEntity<PagedModel<OtherBanksDisputeDto>> searchOtherBankDispute(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, Specification<OtherBanksDispute> search, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                OtherBanksDisputeDto.class, uriBuilder, response, pageable.getPageNumber(), otherBanksDisputeService.searchOtherBankDispute(search,pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<OtherBanksDisputeDto>>(assembler.toModel(otherBanksDisputeService.searchOtherBankDispute(search,pageable).map(otherBanksDisputeMapper::toOtherBanksDisputeDto)), HttpStatus.OK);
    }
}
