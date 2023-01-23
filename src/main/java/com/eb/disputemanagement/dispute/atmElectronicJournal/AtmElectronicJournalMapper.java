package com.eb.disputemanagement.dispute.atmElectronicJournal;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AtmElectronicJournalMapper {
    AtmElectronicJournal toAtmElectronicJournal(AtmElectronicJournalDto atmElectronicJournalsDto);
    AtmElectronicJournalDto toAtmElectronicJournalsDto(AtmElectronicJournal atmElectronicJournal);
}
