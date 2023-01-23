package com.eb.disputemanagement.dispute.jobs.mailsender;


import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
//@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    public JavaMailSender javaMailSender;

    public SendMailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendMailWithAttachment(Mail mail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo(mail.getRecipient());
        mimeMessageHelper.setText(mail.getMessage());
        mimeMessageHelper.setSubject(mail.getSubject());

        FileSystemResource fileSystemResource=
                new FileSystemResource(new File(mail.getUrl()));
        mimeMessageHelper.addAttachment(fileSystemResource.getFilename(),
                fileSystemResource);
        javaMailSender.send(mimeMessage);
        System.out.printf("Mail with attachment sent successfully..");

    }



}