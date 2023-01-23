package com.eb.disputemanagement.dispute.banks;

import com.eb.disputemanagement.dispute.utils.Auditable;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
@Table(name="banks")
@Getter
@Setter
@Where(clause = "deleted=0")
@SQLDelete(sql = "UPDATE banks SET deleted = 1 WHERE id=? and version=?")
public class Bank extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Bank name shouldn't be null")
    @Size(message = "Bank Name should be at least a minimum of 5 characters.", min = 5)
    @Column(name="bank_name")
    private String name;


    @NotNull(message = "Swift Code shouldn't be null")
    @Size(message = "Swift Code  should be at least a minimum of 3 characters.", min = 3)
    @Column(name="swift_code")
    private String swiftCode;
//
//    private boolean deleted = Boolean.FALSE;
//    private String deletedBy;

}
