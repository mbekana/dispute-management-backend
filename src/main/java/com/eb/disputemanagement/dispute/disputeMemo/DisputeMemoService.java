package com.eb.disputemanagement.dispute.disputeMemo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public interface DisputeMemoService {
    DisputeMemo createDisputeMemo(long id, JwtAuthenticationToken token, DisputeMemo disputeMemo) throws IllegalAccessException;
    DisputeMemo getDisputeMemo(long id);
    Page<DisputeMemo> getDisputeMemos(Pageable pageable);
    void deleteDisputeMemo(long id);
    DisputeMemo updateDisputeMemo(long id, DisputeMemo disputeMemo, JwtAuthenticationToken token) throws IllegalAccessException;
}
