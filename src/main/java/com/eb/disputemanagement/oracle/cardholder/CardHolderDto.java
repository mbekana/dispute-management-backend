package com.eb.disputemanagement.oracle.cardholder;

import lombok.Data;

import javax.persistence.Id;

@Data
public class CardHolderDto {
    private String custNumber;
    private@Id String cardNumber;
    private String accountNumber;
    private String customerName;
    private String adress1;
    private String adress2;
    private String adress3;
    private String mobile;
}
