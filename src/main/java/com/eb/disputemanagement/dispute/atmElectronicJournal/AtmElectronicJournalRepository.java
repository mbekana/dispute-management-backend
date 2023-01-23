package com.eb.disputemanagement.dispute.atmElectronicJournal;

import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AtmElectronicJournalRepository extends PagingAndSortingRepository<AtmElectronicJournal, Long>, JpaSpecificationExecutor<AtmElectronicJournal> {

    AtmElectronicJournal findAtmElectronicJournalByDisputeRequestAndDocumentType(DisputeRequest disputeRequest, String documentType);
    @Query("Select fileName from AtmElectronicJournal a where request_id = :requestId")
    String getUploadDocumnetPath(@Param("requestId") Long requestId);


}
