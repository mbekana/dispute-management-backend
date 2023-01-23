package com.eb.disputemanagement.dispute.disputeMemo;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DisputeMemoMapper {
    DisputeMemo toDisputeMemo(DisputeMemoDto disputeMemoDto);
    DisputeMemoDto toDisputeMemoDto(DisputeMemo disputeMemo);
}
