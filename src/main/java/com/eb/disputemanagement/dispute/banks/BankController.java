package com.eb.disputemanagement.dispute.banks;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController implements BankApi {

    private final BankService bankService;
    private final  ApplicationEventPublisher eventPublisher;
    private final BankMapper bankMapper;

    @Override
    public BankDto createBank(@Valid BankDto bankDto) throws IllegalAccessException {
        return  bankMapper.toBankDto(bankService
                .createBank(bankMapper.toBank(bankDto)));
    }

    @Override
    public BankDto getBank(long id) {
        return bankMapper.toBankDto(bankService.getBank(id));
    }

    @Override
    public BankDto updateBank(long id, BankDto bankDto) {
        return bankMapper.toBankDto(bankService.updateBank(id, bankMapper.toBank(bankDto)));
    }

    @Override
    public ResponseEntity<PagedModel<BankDto>> getBanks(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                BankDto.class, uriBuilder, response, pageable.getPageNumber(), bankService.getBanks(pageable).getTotalPages(), pageable.getPageSize()));

        return new ResponseEntity<PagedModel<BankDto>>(assembler.toModel(bankService.getBanks(pageable).map(bankMapper::toBankDto)), HttpStatus.OK);
    }

    @Override
    public void deleteBank(long id, JwtAuthenticationToken token) {
        bankService.deleteBank(id, token);
    }

    @Override
    public void existsBySwiftCode(String swiftCode) {
        bankService.existsBySwiftCode(swiftCode);
    }

    @Override
    public ResponseEntity<PagedModel<BankDto>> searchBank(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, Specification<Bank> search, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                BankDto.class, uriBuilder, response, pageable.getPageNumber(), bankService.searchBank(search,pageable).getTotalPages(), pageable.getPageSize()));
        return new ResponseEntity<PagedModel<BankDto>>(assembler.toModel(bankService.searchBank(search,pageable).map(bankMapper::toBankDto)), HttpStatus.OK);
    }
}
