package com.eb.disputemanagement.oracle.cardholder;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Repository
@Transactional
public interface CardHolderRepository extends JpaRepository<CardHolder, Long>{
    Page<CardHolder> findOneCardHolder(String accountNumber, Pageable pageable);
}
