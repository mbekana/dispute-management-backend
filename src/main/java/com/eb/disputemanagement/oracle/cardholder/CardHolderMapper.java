package com.eb.disputemanagement.oracle.cardholder;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderMapper {
    CardHolder toCardHolder(CardHolderDto cardHolderDto);
    CardHolderDto toCardHolderDto(CardHolder cardHolder);
}
