package com.eb.disputemanagement.dispute.comment;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
public class CommentDto extends Auditable {
    private  Long id;
    private Employee senderId;
    private  String content;
    private  CommentStatus status;
    private  Employee readBy;
    @JsonIgnore
    private DisputeRequest disputeRequest;

}
