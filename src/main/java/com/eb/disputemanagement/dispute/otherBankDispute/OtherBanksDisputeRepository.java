package com.eb.disputemanagement.dispute.otherBankDispute;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherBanksDisputeRepository extends PagingAndSortingRepository<OtherBanksDispute, Long>, JpaSpecificationExecutor<OtherBanksDispute> {
}
