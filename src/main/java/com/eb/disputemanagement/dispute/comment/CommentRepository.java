package com.eb.disputemanagement.dispute.comment;

import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findAllByStatus(CommentStatus status);
    Page<Comment> findAllByDisputeRequest(DisputeRequest disputeRequest, Pageable pageable);
}
