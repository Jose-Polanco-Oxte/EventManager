package jpolanco.springbootapp.user.infrastructure.services.implementations;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailServieImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServieImpl.class);

    private SendGrid sg;
    @Value("${api.corp.email}")
    private String corporateEmail;

    public EmailServieImpl(@Value("${api.email.key}") String apiKey) {
        this.sg = new SendGrid(apiKey);
    }
    @Override
    public void sendVerificationEmail(String to) {
        Email from = new Email(corporateEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", "I'm replacing the <strong>body tag</strong>");
        Mail mail = new Mail(from, "Welcome to EM", toEmail, content);
        mail.setTemplateId("d-4e59fccfa29d4510b13a06decc0994da");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info("status to send email: {}", response.getStatusCode());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void sendPasswordResetEmail(String recipient, String subject, String body) {

    }
}
