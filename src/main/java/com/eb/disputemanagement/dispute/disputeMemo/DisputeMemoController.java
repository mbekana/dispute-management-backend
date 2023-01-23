package com.eb.disputemanagement.dispute.disputeMemo;

import com.eb.disputemanagement.dispute.banks.BankDto;
import com.eb.disputemanagement.dispute.utils.PaginatedResultsRetrievedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1/dispute-memos")
@RequiredArgsConstructor
public class DisputeMemoController implements DisputeMemoApi{

    private final DisputeMemoMapper disputeMemoMapper;

    private final DisputeMemoService disputeMemoService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public DisputeMemoDto createDisputeMemo(long id, JwtAuthenticationToken token, DisputeMemoDto disputeMemoDto) throws IllegalAccessException {
        return disputeMemoMapper.toDisputeMemoDto(disputeMemoService.createDisputeMemo(id, token,  disputeMemoMapper.toDisputeMemo(disputeMemoDto)));
    }

    @Override
    public DisputeMemoDto getDisputeMemo(long id) {
        return disputeMemoMapper.toDisputeMemoDto(disputeMemoService.getDisputeMemo(id));
    }

    @Override
    public ResponseEntity<PagedModel<DisputeMemoDto>> getDisputeMemos(Pageable pageable, PagedResourcesAssembler assembler, UriComponentsBuilder uriBuilder, HttpServletResponse response) {
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                BankDto.class, uriBuilder, response, pageable.getPageNumber(), disputeMemoService.getDisputeMemos(pageable).getTotalPages(), pageable.getPageSize()));

        return new ResponseEntity<PagedModel<DisputeMemoDto>>(assembler.toModel(disputeMemoService.getDisputeMemos(pageable).map(disputeMemoMapper::toDisputeMemoDto)), HttpStatus.OK);
    }

    @Override
    public DisputeMemoDto updateDisputeMemo(long id, DisputeMemoDto disputeMemoDto, JwtAuthenticationToken token) throws IllegalAccessException {
        return disputeMemoMapper.toDisputeMemoDto(disputeMemoService.updateDisputeMemo(id, disputeMemoMapper.toDisputeMemo(disputeMemoDto), token));
    }

    @Override
    public void deleteDisputeMemo(long id) {
        disputeMemoService.deleteDisputeMemo(id);
    }
}
