package co.payrail.attendance_srv.integration.service;

import co.payrail.attendance_srv.auth.entity.Email;
import org.springframework.mail.MailException;
import org.thymeleaf.context.Context;

import java.io.IOException;

public interface MailService {

    void sendGrid(Email emailInfo) throws IOException;

    void sendGridMail(Email email, Context context) throws IOException;

    void sendAwsEmail(Email email, Context context) throws IOException;

    void sendGridwithSenderMail(Email emailInfo, Context context, String mailSender) throws IOException;

    void sendGridMailWithAttachment(Email emailInfo, Context context, String fileName, String fileLocation) throws IOException;




}

