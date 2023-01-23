package com.eb.disputemanagement.dispute.exceptions;

public class PasswordMisMatchException extends RuntimeException {
   private String message;

    public PasswordMisMatchException(String message) {
        super(message);

    }
}
