package com.eb.disputemanagement.oracle.cardholder;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CardHolderServiceImpl implements  CardHolderService{
    @Autowired
    CardHolderRepository cardHolderRepository;

    @Override
    public Page<CardHolder> getCardHolder(String accountNumber, Pageable pageable) throws IllegalAccessException {
        if(cardHolderRepository.findOneCardHolder(accountNumber, pageable).isEmpty()){
            throw new IllegalAccessException("No card number can be found with that account number");
        }
        return cardHolderRepository.findOneCardHolder(accountNumber, pageable);
    }
}
