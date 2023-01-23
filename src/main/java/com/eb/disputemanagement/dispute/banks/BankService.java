package com.eb.disputemanagement.dispute.banks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public interface BankService {
    Page<Bank> getBanks(Pageable pageable);
    Bank createBank(Bank bank) throws IllegalAccessException;
    Bank updateBank(long id, Bank bank);
    void deleteBank(long id, JwtAuthenticationToken token);
    Bank getBank(long id);
    boolean existsBySwiftCode(String swiftCode);
    Bank getBySwiftCode(String swiftCode);

    Page<Bank> searchBank(Specification<Bank> search, Pageable pageable);
}
