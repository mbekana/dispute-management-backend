package com.eb.disputemanagement.dispute.disputeRequest;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.atmElectronicJournal.AtmElectronicJournal;
import com.eb.disputemanagement.dispute.banks.Bank;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import com.eb.disputemanagement.dispute.disputeMemo.DisputeMemo;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DisputeRequestDto extends Auditable {
    private  Long id;

    @NotNull(message = "Card Number can Not be null")
    @Size(min = 19, max = 19)
    private  String cardNumber;

    @NotNull(message = "Account Number can Not be null")
    @Size(min = 16, max = 16)
    private  String accountNumber;

    private  String address;
    private DRequestType requestType;

    @NotNull(message = "Phone Number can Not be null")
    private  String telephone;

    @NotNull(message = "Transaction Date can Not be null")
    private  LocalDateTime transactionDate;

    @NotNull(message = "Transaction Amount can Not be null")
    private  BigDecimal transactionAmount;

    private Bank bankTerminal;
    private  LocalDateTime approvedDate;
    private  LocalDateTime settlementDate;
    private  String settledBy;
    private  LocalDate receivedDate;
    private Employee receivedBy;
    private  String feNumber;
    private String disputeId;

    private  String terminalOwnerBranch;
    private  boolean newRequest;
    private  boolean customerStatement;

    @NotNull(message = "Card Holder Name Name can Not be null")
    private  String cardHolderName;

    private  boolean declined;
    private  String declinedBy;
    private  boolean acknowledged;
    private  String acknowledgedBy;
    private  boolean approved;
    private  boolean received;
    private  String approvedBy;
    private DisputeMemo disputeMemo;
    private RequestStatus status;
    private  String declineReason;
    private  boolean mailSent;
    private String payerBranch;
    private String payerBranchName;
    private Employee maker;
    private AtmElectronicJournal atmElectronicJournal;

}
