package com.eb.disputemanagement.dispute.otherBankDispute;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OtherBanksDisputeMapper {
    OtherBanksDispute toOtherBanksDispute(OtherBanksDisputeDto otherBanksDisputeDto);
    OtherBanksDisputeDto toOtherBanksDisputeDto(OtherBanksDispute otherBanksDispute);
}
