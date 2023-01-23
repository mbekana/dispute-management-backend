package com.eb.disputemanagement.dispute.otherBankDispute;

import com.eb.disputemanagement.dispute.banks.BankDto;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import com.eb.disputemanagement.dispute.dto.EmployeeDto;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class OtherBanksDisputeDto extends Auditable {
    private  Long Id;
    @NotNull(message = "Issuer Bank Can not be null")
    private BankDto issuerBank;
    @NotNull(message = "Aquirer bank can not be null")
    private  BankDto acquirerBank;
    @NotNull(message = "Card Number can not be null!")
    @Size(message = "Card Number should be a minimum of 19 length", min = 19, max = 19)
    private  String cardNumber;
    @NotNull(message = "Amount can not be null!")
    private  String amount;
    @NotNull(message = "Amount can not be null!")
    private  LocalDateTime transactionDate;
    private  String ejStatus;
    private  String terminalId;
    private DRequestType requestType;
    private RequestStatus status;
    private EmployeeDto employee;
    private  String feNumber;
    private  String disputeId;
//    private boolean deleted=Boolean.FALSE;
//    private String deletedBy;
}
