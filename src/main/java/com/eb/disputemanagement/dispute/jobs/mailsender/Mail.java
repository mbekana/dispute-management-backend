package com.eb.disputemanagement.dispute.jobs.mailsender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Mail {
    private String recipient;
    private String subject;
    private String message;
    private String url;

}
