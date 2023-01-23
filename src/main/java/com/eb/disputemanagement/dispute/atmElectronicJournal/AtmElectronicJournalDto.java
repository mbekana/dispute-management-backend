package com.eb.disputemanagement.dispute.atmElectronicJournal;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
public class AtmElectronicJournalDto extends Auditable {
    private  Long id;
    private  String fileName;
    private  String documentType;
    private  String documentFormat;
    private  String uploadDir;
    private Employee uploadedBy;
    @JsonIgnore
    private DisputeRequest disputeRequest;
}
