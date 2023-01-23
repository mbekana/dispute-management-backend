package com.eb.disputemanagement.dispute.exceptions;

import lombok.Getter;

@Getter
public class UnAuthorizedActionException extends RuntimeException {
    private final String message;

    public UnAuthorizedActionException(String msg) {
        this.message = msg;
    }

}
