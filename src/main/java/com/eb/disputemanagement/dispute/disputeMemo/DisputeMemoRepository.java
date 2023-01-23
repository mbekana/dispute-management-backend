package com.eb.disputemanagement.dispute.disputeMemo;

import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeMemoRepository extends PagingAndSortingRepository<DisputeMemo, Long>{
     boolean existsByDisputeRequest(DisputeRequest disputeRequest);
     DisputeMemo findByDisputeRequest(DisputeRequest disputeRequest);
}
