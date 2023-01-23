package com.eb.disputemanagement.dispute.dto;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Contact;
import lombok.Data;

@Data
public class EmployeeDto {
    private String employeeId;
    private String fullName;
    private Contact Contact;
    private BranchDto branch;
}
