package com.eb.disputemanagement.dispute.dto;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}