package com.eb.disputemanagement.dispute.banks;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface BankRepository extends PagingAndSortingRepository<Bank, Long>, JpaSpecificationExecutor<Bank>{
    boolean existsBySwiftCode( String swiftCode);
    Bank findBySwiftCode(String swiftCode);
}
