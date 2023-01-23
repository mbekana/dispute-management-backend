package com.eb.disputemanagement.dispute.disputeRequest;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DisputeRequestMapper {
    DisputeRequest toDisputeRequest(DisputeRequestDto disputeRequestDto);
    DisputeRequestDto toDisputeRequestDto(DisputeRequest disputeRequest);
}
