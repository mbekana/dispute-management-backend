package com.eb.disputemanagement.dispute.comment;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.utils.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "comments")
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE comments SET deleted = 1 WHERE id=? and  version=?")
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "sender_employee_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "sender_fullname")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "sender_mail")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "sender_branch_code")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "sender_branch_name")),
    })
    private Employee senderId;
    private String content;
    private CommentStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "read_by_employee_id")),
            @AttributeOverride(name = "fullName", column = @Column(name = "read_by_employee_fullname")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "read_by_employee_mail")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "read_by_branch_code")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "read_by_employee_branch_name")),
    })
    private Employee readBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="request_id", nullable=false)
    private DisputeRequest disputeRequest;
}
