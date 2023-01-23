package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.banks.Bank;
import com.eb.disputemanagement.dispute.atmElectronicJournal.AtmElectronicJournal;

import com.eb.disputemanagement.dispute.comment.Comment;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import com.eb.disputemanagement.dispute.disputeMemo.DisputeMemo;
import com.eb.disputemanagement.dispute.notification.Notification;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dispute_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE dispute_requests SET deleted = 1 WHERE id=? and version=?")
public class DisputeRequest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cardNumber")
    @NotNull(message="Card Number can Not be null")
    @Size(min = 19, max = 19)
    private String cardNumber;

    @Column(name = "accountNumber")
    @NotNull(message="Account Number can Not be null")
    @Size(min = 16, max = 16)
    private String accountNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "requestType")
    @Enumerated(EnumType.STRING)
    private DRequestType requestType;

    @Column(name = "telephone")
    @NotNull(message="Phone Number can Not be null")
    private String telephone;

    @NotNull(message="Transaction Date can Not be null")
    @Column(name = "transactionDate")
    private LocalDateTime transactionDate;

    @Column(name = "transactionAmount")
    @NotNull(message="Transaction Amount can Not be null")
    private BigDecimal transactionAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bankTerminal",  referencedColumnName = "id")
    @NotNull(message="Bank Can not be null!")
    private Bank bankTerminal;

    @Column(name = "approvedDate")
    private LocalDateTime approvedDate;

    @Column(name = "settlementDate")
    private LocalDateTime settlementDate;

    @Column(name = "settledBy")
    private String settledBy;

    private LocalDate receivedDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "receiver_employee_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "receiver_employee_full_name")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "receiver_branch_code")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "receiver_branch_name")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "receiver_email")),
    })
    private Employee receivedBy;

    private String feNumber;
    private String disputeId;
    private String terminalOwnerBranch;
    private boolean newRequest = Boolean.TRUE;

    @Column(name = "customerStatement")
    private boolean customerStatement = Boolean.FALSE;

    @Column(name = "cardHolderName")
    @NotNull(message="Card Holder Name Name can Not be null")
    private String cardHolderName;

    @OneToMany(mappedBy = "disputeRequest", cascade = CascadeType.ALL)
    List<Notification> notifications = new ArrayList<>();

    @Column(name = "declined")
    private boolean declined = Boolean.FALSE;

    @Column(name = "declinedBy")
    private String declinedBy;

    @Column(name = "acknowledged")
    private boolean acknowledged = Boolean.FALSE;

    @Column(name = "acknowledgedBy")
    private String acknowledgedBy;

    @Column(name = "approved")
    private boolean approved = Boolean.FALSE;

    @Column(name = "received")
    private boolean received = Boolean.FALSE;

    @Column(name = "approvedBy")
    private String approvedBy;

    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "declineReason")
    private String declineReason;

    @Column(name = "mail_sent")
    private boolean mailSent = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dispute_memo", referencedColumnName = "id")
    private DisputeMemo disputeMemo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "aej_id", referencedColumnName = "id")
    private AtmElectronicJournal atmElectronicJournal;

    @OneToMany(mappedBy = "disputeRequest", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "maker_employee_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "maker_employee_fullName")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "maker_branch_code")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "maker_branch_name")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "employee_email")),
    })
    private Employee maker;

    private String payerBranch;
    private String payerBranchName;

}
