package com.eb.disputemanagement.oracle.cardholder;

import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestDto;
import com.eb.disputemanagement.dispute.utils.PaginatedResultsRetrievedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/card-holder-details")
@RequiredArgsConstructor
public class CardHolderController implements CardHolderApi{
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderService cardHolderService;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    public ResponseEntity<PagedModel<CardHolderDto>> getComments(Pageable pageable, PagedResourcesAssembler assembler, String accountNumber, UriComponentsBuilder uriBuilder, HttpServletResponse response) throws IllegalAccessException {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                DisputeRequestDto.class, uriBuilder, response, pageable.getPageNumber(), cardHolderService.getCardHolder(accountNumber, pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<CardHolderDto>>(assembler.toModel(cardHolderService.getCardHolder(accountNumber, pageable).map(cardHolderMapper::toCardHolderDto)), HttpStatus.OK);
    }
}
