package com.eb.disputemanagement.dispute.otherBankDispute;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.banks.Bank;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "other_bank_disputes")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE other_bank_disputes SET deleted = 1 WHERE id=? and  version=?")
public class OtherBanksDispute extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issuerBank",  referencedColumnName = "id")
    @NotNull(message = "Issuer Bank Can Not be Null")
    private Bank issuerBank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acquirerBank",  referencedColumnName = "id")
    @NotNull(message = "Acquirer Bank Can Not be Null")
    private Bank acquirerBank;


    @NotNull(message = "Card Number can not be null!")
    @Size(message = "Card Number should be a minimum of 19 length", min=19, max=19)
    private String cardNumber;

    @NotNull(message = "Amount can not be null!")
    private String amount;

    @NotNull(message = "Amount can not be null!")
    private LocalDateTime transactionDate;

    private String ejStatus;

    private String terminalId;

    private DRequestType requestType;

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

    @Column(name = "feNumber")
    private String feNumber;

    @Column(name = "disputeId")
    private String disputeId;

//    private boolean deleted=Boolean.FALSE;
//    private String deletedBy;


}
