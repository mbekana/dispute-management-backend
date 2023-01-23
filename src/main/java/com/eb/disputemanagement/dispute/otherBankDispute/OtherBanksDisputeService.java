package com.eb.disputemanagement.dispute.otherBankDispute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public interface OtherBanksDisputeService {
    Page<OtherBanksDispute> getOtherBanksDisputes(Pageable pageable);
    OtherBanksDispute createOtherBanksDispute(JwtAuthenticationToken token, OtherBanksDispute otherBanksDispute);
    OtherBanksDispute updateOtherBanksDispute(long id, JwtAuthenticationToken token,OtherBanksDispute otherBanksDispute) throws IllegalAccessException;
    void deleteOtherBanksDispute(long id);
    OtherBanksDispute getOtherBanksDispute(long id);

    Page<OtherBanksDispute> searchOtherBankDispute(Specification<OtherBanksDispute> search, Pageable pageable);
}
