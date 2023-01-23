package com.eb.disputemanagement.dispute.banks;

import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.Data;

@Data
public class BankDto extends Auditable {
    private Long id;
    private String name;
    private String swiftCode;
}
