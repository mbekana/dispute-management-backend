package com.eb.disputemanagement.oracle.cardholder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "SWTM_CARD_DETAILS")
@NamedNativeQuery(name = "CardHolder.findOneCardHolder", query = "select * from cardholders where accountNumber=?", resultSetMapping = "CardHolder")
@SqlResultSetMapping(name = "CardHolder",
        classes = @ConstructorResult(targetClass = CardHolder.class,
                columns = {
                        @ColumnResult(name = "custNumber", type = String.class),
                        @ColumnResult(name = "cardNumber", type = String.class),
                        @ColumnResult(name = "accountNumber", type = String.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name="adress1", type = String.class),
                        @ColumnResult(name="adress2", type = String.class),
                        @ColumnResult(name="adress3", type = String.class),
                        @ColumnResult(name="mobile", type = String.class)
                }))
@NoArgsConstructor
@AllArgsConstructor
public class CardHolder implements Serializable {
    private String custNumber;
    private@Id String cardNumber;
    private String accountNumber;
    private String customerName;
    private String adress1;
    private String adress2;
    private String adress3;
    private String mobile;
}
