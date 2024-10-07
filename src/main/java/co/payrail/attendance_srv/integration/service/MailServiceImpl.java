package co.payrail.attendance_srv.integration.service;

import co.payrail.attendance_srv.auth.entity.Email;
import co.payrail.attendance_srv.integration.service.awsemail.EmailSenderService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MailServiceImpl implements MailService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    EmailSenderService emailSenderService;

    @Value("${mail.from}")
    private String sender;

    @Value("${SENDGRID_API_KEY}")
    private String SENDGRID_API_KEY;

    @Value("${mail.from.name}")
    private String senderName;


    @Override
    public void sendGrid(Email emailInfo) throws IOException {
        logger.info("Recipients >> ", emailInfo.getReceiverEmails());
        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(sender, senderName);
        String subject = emailInfo.getMessageSubject();
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(emailInfo.getReceiverEmail());
        Content content = new Content("text/plain", emailInfo.getMessageBody());
        Mail mail = new Mail(from, subject, to, content);

        if (emailInfo.getReceiverEmails().length > 0 ) {
            Personalization personalization = null;
            for (int i = 1, size = emailInfo.getReceiverEmails().length; i < size; i++) {
                personalization = new Personalization();
                personalization.addTo(new com.sendgrid.helpers.mail.objects.Email(emailInfo.getReceiverEmails()[i]));
                mail.addPersonalization(personalization);
            }
        }

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            com.sendgrid.Response response = sg.api(request);
            System.out.println("MAIL SENT");
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }

    @Override
    public void sendGridMail(Email emailInfo, Context context) throws IOException {
        String messageBody = templateEngine.process(emailInfo.getTemplate(), context);

        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(sender, senderName);
        String subject = emailInfo.getMessageSubject();
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(emailInfo.getReceiverEmail());
        Content content = new Content("text/html", messageBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            request.setBody(mail.build());
            com.sendgrid.Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            log.info("Sent");
        } catch (IOException ex) {
            throw ex;
        }
    }

    @Override
    public void sendAwsEmail(Email emailInfo, Context context) throws IOException {
        String messageBody = templateEngine.process(emailInfo.getTemplate(), context);
        List<String> rlist = new ArrayList<>();
        if(!StringUtils.isEmpty(emailInfo.getReceiverEmail())){
            rlist.add(emailInfo.getReceiverEmail());
        }
        if(!Objects.isNull(emailInfo.getReceiverEmails()) && emailInfo.getReceiverEmails().length<1){
            Collections.addAll(rlist, emailInfo.getReceiverEmails());
        }
        List<String> cclist = new ArrayList<>();
        if(!Objects.isNull(emailInfo.getCcList()) && emailInfo.getCcList().length<1){
            Collections.addAll(cclist, emailInfo.getCcList());
        }
        List<String> bcclist = new ArrayList<>();
        bcclist.add("wsowunmi@plethub.com");
        bcclist.add("komirin@plethub.com");
        emailSenderService.sendAWSHTMLEmail("Welcome to PayRail", rlist, cclist, bcclist, messageBody);
    }

    @Override
    public void sendGridwithSenderMail(Email emailInfo, Context context, String mailSender) throws IOException {
        String messageBody = templateEngine.process(emailInfo.getTemplate(), context);

        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(mailSender);
        String subject = emailInfo.getMessageSubject();
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(emailInfo.getReceiverEmail());
        Content content = new Content("text/html", messageBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            request.setBody(mail.build());
            com.sendgrid.Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }

    @Override
    public void sendGridMailWithAttachment(Email emailInfo, Context context,String fileName , String fileLocation) throws IOException {

        String messageBody = templateEngine.process(emailInfo.getTemplate(), context);

        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(sender,senderName);
        String subject = emailInfo.getMessageSubject();
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(emailInfo.getReceiverEmail());
        Content content = new Content("text/html", messageBody);
        Mail mail = new Mail(from, subject, to, content);

        InputStream inputStream =  this.getClass().getClassLoader().getResourceAsStream(fileLocation);

        if(Objects.isNull(inputStream)){
            logger.info("location is : {}", fileLocation);
            logger.info("input is null!!!!!!!");
        }

        assert inputStream != null;
        Attachments attachments = new Attachments.Builder(fileName,inputStream).withType("application/pdf").build();


        mail.addAttachments(attachments);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {


            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            request.setBody(mail.build());
            com.sendgrid.Response response = sg.api(request);
            log.info("Response ==> {}", response);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }










}