package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface DisputeRequestService {
    DisputeRequest createDisputeRequest(JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException;
    Page<DisputeRequest> getDisputeRequests(Pageable pageable);
    DisputeRequest getDisputeRequest(long id);
    DisputeRequest updateDisputeRequest(long id, DisputeRequest disputeRequest, JwtAuthenticationToken token) throws IllegalAccessException;
    void deleteDisputeRequest(long id, JwtAuthenticationToken token);
    boolean pendingRequest(String cardNumber, LocalDateTime transactionDate, BigDecimal TransactionAmount, String RequesterName, RequestStatus status);
    DisputeRequest receiveDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException;
    DisputeRequest approveRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException;
    DisputeRequest settleRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException;
    DisputeRequest markAsNotFound(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest);
    DisputeRequest acknowledgeRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest) throws IllegalAccessException;
    Page<DisputeRequest> getByRequestInitiator(JwtAuthenticationToken token, Pageable pageable);
    Page<DisputeRequest> getDisputeByRequestTypeAndStatus(DRequestType requestType, RequestStatus status, Pageable pageable);
    Page<DisputeRequest> getByRequestInitiatorBranchAndRequestType(JwtAuthenticationToken token, DRequestType requestType, Pageable pageable);
    Page<DisputeRequest> getLastFiveNewDisputeRequests(JwtAuthenticationToken token, boolean newRequest, Pageable pageable);
    Page<DisputeRequest> getByPayerBranch(JwtAuthenticationToken token, Pageable pageable);
    Page<DisputeRequest> getByBranchCode(JwtAuthenticationToken token, Pageable pageable);
    Page<DisputeRequest> getExpiringRequests(boolean received, DRequestType requestType, RequestStatus status, LocalDate receivedDate, Pageable pageable);
    Page<DisputeRequest> getByAcknowledgedAndRequestTypeAndStatus(boolean acknowledged, DRequestType requestType, RequestStatus status, Pageable pageable);
    DisputeRequest declineDisputeRequest(long id, JwtAuthenticationToken token, DisputeRequest disputeRequest);
    boolean existsByFeNumberAndDisputeId(String feNumber, String disputeId);
    Page<DisputeRequest> searchDisputeRequest(Specification<DisputeRequest> search, Pageable pageable);
    List<DisputeRequest> findByReceivedAndRequestTypeAndStatusAndMailSent(boolean received, DRequestType requestType, RequestStatus status, boolean mailSent);
    Page<DisputeRequest> getFirst5ByNewRequest(boolean newRequest, Pageable pageable);
    
    DisputeRequest confirmSettlement(long id, JwtAuthenticationToken settledBy);

    Page<DisputeRequest> getAcknowledgedDisputeRequests(boolean acknowledged, Pageable pageable);

}
