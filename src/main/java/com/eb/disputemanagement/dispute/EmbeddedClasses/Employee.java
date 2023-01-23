package com.eb.disputemanagement.dispute.EmbeddedClasses;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String employeeId;
    private String fullName;
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "email", column = @Column(name = "email"))
    })
    private Contact contact;
    @Embedded()
    @AttributeOverrides({@AttributeOverride(name = "code", column = @Column(name = "branch_code"))
            , @AttributeOverride(name = "name", column = @Column(name = "branch_name"))})
    private Branch branch;
}
