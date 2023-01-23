package com.eb.disputemanagement.dispute.disputeMemo;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.utils.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "dispute_memos")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE dispute_memos SET deleted = 1 WHERE id=? and  version=?")
public class DisputeMemo extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String failedMachineName;
    private Double cashOnATM;
    private Double networkSuspenseGl;
    private Double networkSuspenseGl2;
    private Double creditCustomersAC;
    private Double atmCashWithdrawalFee;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="dispute_request_id")
    @JsonIgnore
    private DisputeRequest disputeRequest;

    private String payerBranch;
    private String payerBranchName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "request_initiator_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "request_initiator_full_name")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "request_initiator_branch_code")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "request_initiator_branch_name")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "request_initiator_emial")),
    })
    @Column(name = "request_initiator")
    private Employee employee;

}
