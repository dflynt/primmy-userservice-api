package org.dflynt.primmy.userservice.services;

import org.dflynt.primmy.userservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    JavaMailSender jms;

    public EmailService(){
    }

    public boolean sendVerificationEmail(User u) {
        MimeMessage msg = new JavaMailSenderImpl().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(u.getEmail());
            helper.setSubject("Primmy Verification Email");
            String content = "You're almost there, [user]!<br>" +
                    "Please click the following link to verify your email.<br>" +
                    "<h3><a href=\"http://localhost:4200/verify/[params]\">VERIFY</a></h3>" +
                    "Thank you,<br>" +
                    "The Primmy Team.";

            //construct the parameters
            String params = u.getUserid() + "/" + u.getVerificationCode();

            //replace placeholders with real values
            content = content.replace("[user]", u.getFirstName());
            content = content.replace("[params]", params);

            helper.setText(content, true);

            jms.send(msg);

            return true;
        }
        catch(MessagingException e) {
            return false;
        }
    }
}
