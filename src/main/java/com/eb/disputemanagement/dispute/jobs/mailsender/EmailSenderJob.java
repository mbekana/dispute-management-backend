package com.eb.disputemanagement.dispute.jobs.mailsender;



import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequest;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestRepository;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestService;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.common.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;


@Service
@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.db.disputemanagement.dispute.jobs.mailsender")
//@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class EmailSenderJob {

    private final SendMailService service;
    private final DisputeRequestService disputeRequestService;
    private final DisputeRequestRepository disputeRequestRepository;

    Logger logger = LoggerFactory.getLogger(EmailSenderJob.class);

    @Scheduled(cron = "0 0 9,14 * * MON-SAT")
//    @Scheduled(cron = "*/5 * * * * *")
    public void runEmailJob() throws MessagingException {
        var dr = disputeRequestService.findByReceivedAndRequestTypeAndStatusAndMailSent(true, DRequestType.ENAT_ON_OTHER_BANK, RequestStatus.PROCESSING, false);
        LocalDate today = LocalDate.now();
        for (DisputeRequest r : dr) {
            LocalDate due = r.getReceivedDate().plusDays(6);
            logger.info(String.valueOf(today.equals(due)));
            if(today.equals(due)){
                r.setMailSent(true);
                disputeRequestRepository.save(r);
                Mail mail = new Mail(
                        r.getReceivedBy().getContact().getEmail(),
                        "Dispute Issue Expire Date",
                        "Dear " + r.getReceivedBy().getFullName()
                                + " the ATM dispension request with Id: "
                                + r.getId()
                                + ", Dispute Id: "
                                + r.getDisputeId()
                                + " and FE Number: " + r.getFeNumber()
                                + " Will be expired after two days. Please take your time to check it!",
                        "src/main/resources/Dispute_Newsletter.pdf");
                System.out.println(mail);
                service.sendMailWithAttachment(mail);
            }

        }
    }

}

