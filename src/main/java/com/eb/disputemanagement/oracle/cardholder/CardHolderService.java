package com.eb.disputemanagement.oracle.cardholder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CardHolderService {
    Page<CardHolder> getCardHolder(String accountNumber, Pageable pageable) throws IllegalAccessException;
}
