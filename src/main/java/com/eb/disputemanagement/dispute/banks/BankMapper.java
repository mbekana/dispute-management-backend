package com.eb.disputemanagement.dispute.banks;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMapper {
    Bank toBank(BankDto bankDto);
    BankDto toBankDto(Bank bank);
}
