package com.eb.disputemanagement.dispute.disputeMemo;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.Data;

@Data
public class DisputeMemoDto extends Auditable {
    private  Long id;
    private  String failedMachineName;
    private  Double cashOnATM;
    private  Double networkSuspenseGl;
    private  Double networkSuspenseGl2;
    private  Double creditCustomersAC;
    private  Double atmCashWithdrawalFee;
    private String payerBranch;
    private String payerBranchName;
    private Employee employee;

}
