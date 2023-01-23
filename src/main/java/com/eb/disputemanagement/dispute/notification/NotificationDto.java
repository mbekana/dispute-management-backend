package com.eb.disputemanagement.dispute.notification;


import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
public class NotificationDto implements Serializable {
    private  Long id;
    private  String disputeId;
    private  NotificationStatus status;
    private Employee readBy;
    private  String content;
    private  Long request_id;
    private  NotificationType notificationType;
    @JsonIgnore
    private DisputeRequest disputeRequest;
    private  String requestInitiatorBranch;
    private  String targetBranch;
}
