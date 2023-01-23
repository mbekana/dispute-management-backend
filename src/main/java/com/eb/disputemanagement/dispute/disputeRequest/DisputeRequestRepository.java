package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RepositoryRestResource
public interface DisputeRequestRepository extends PagingAndSortingRepository<DisputeRequest, Long>, JpaSpecificationExecutor<DisputeRequest> {
    boolean existsByCardNumberAndTransactionDateAndTransactionAmountAndCardHolderNameAndStatus(
            String cardNumber, LocalDateTime transactionDate, BigDecimal TransactionAmount, String cardHolderName, RequestStatus status);

    Page<DisputeRequest> findByMaker_EmployeeIdOrderByUpdatedAtDesc(String maker, Pageable pageable);
    Page<DisputeRequest> findByMaker_EmployeeIdAndRequestType(String maker, DRequestType requestType, Pageable pageable);
    Page<DisputeRequest> findByRequestTypeAndStatus(DRequestType requestType, RequestStatus status, Pageable pageable);
    Page<DisputeRequest> findByMaker_BranchCodeAndRequestType(String branch, DRequestType requestType, Pageable pageable);
    Page<DisputeRequest> findByMaker_BranchCode(String branch, Pageable pageable);
    Page<DisputeRequest> findFirst5ByMaker_BranchCodeAndNewRequest(String branch, boolean newRequest, Pageable pageable);
    Page<DisputeRequest> findFirst5ByNewRequest(boolean newRequest, Pageable pageable);
    Page<DisputeRequest> findByTerminalOwnerBranch(String terminalOwnerBranch, Pageable pageable);
    Page<DisputeRequest> findByReceivedAndRequestTypeAndStatusAndReceivedDate(boolean received, DRequestType requestType, RequestStatus status, LocalDate receivedDate, Pageable pageable);
    Page<DisputeRequest> findByAcknowledgedAndRequestTypeAndStatus(boolean acknowledged, DRequestType requestType, RequestStatus status, Pageable pageable);
    boolean existsByFeNumberAndDisputeId(String feNumber, String disputeId);

    Page<DisputeRequest> findByAcknowledged(boolean acknowledged, Pageable pageable);
    List<DisputeRequest> findByReceivedAndRequestTypeAndStatusAndMailSent(boolean received, DRequestType requestType, RequestStatus status, boolean mailSent);
}
