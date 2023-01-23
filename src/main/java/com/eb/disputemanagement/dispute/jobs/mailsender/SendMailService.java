package com.eb.disputemanagement.dispute.jobs.mailsender;


import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
@Service
public interface SendMailService {
      void sendMailWithAttachment(Mail mail) throws MessagingException;
}

