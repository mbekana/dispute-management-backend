package com.eb.disputemanagement.dispute.notification;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.utils.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;


@Entity
@Table(name = "notifications")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE notifications SET deleted = 1 WHERE id=? and  version=?")
public class Notification  extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private NotificationStatus status;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "reader_employee_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "reader_employee_fullName")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "reader_branch_code")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "reader_branch_name")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "reader_employee_email")),
    })
    private Employee readBy;
    private String content;
    private NotificationType notificationType;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="disputeRequest_id", referencedColumnName = "id", nullable=false)
    private DisputeRequest disputeRequest;
    private String requestInitiatorBranch;
    private String targetBranch;

}
