package com.eb.disputemanagement.dispute.atmElectronicJournal;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.utils.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;

@Entity
@Table(name = "ej_files")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ej")
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE ej_files SET deleted = 1 WHERE id=? and  version=?")
public class AtmElectronicJournal extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "upload_dir")
    private String uploadDir;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "uploaded_by_employeeId")),
            @AttributeOverride(name = "branch.code", column = @Column(name = "employee_branch_code ")),
            @AttributeOverride(name = "branch.name", column = @Column(name = "employee_branch_name")),
            @AttributeOverride(name = "contact.email", column = @Column(name = "employee_email")),
    })
    private Employee uploadedBy;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="request_id")
    @JsonIgnore
    private DisputeRequest disputeRequest;

}
