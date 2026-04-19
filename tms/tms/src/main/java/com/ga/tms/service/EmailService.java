package com.ga.tms.service;
import com.resend.*;
import com.resend.services.emails.model.SendEmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendVerificationEmail(String to, String verificationLink){
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("noreply@resend.dev")
                .to(to)
                .subject("Verify your email")
                .html("""
                        Welcome to TMS!<br>
                        <br>
                        Please verify your email by clicking the link below:<br>
                        <br><a href=
                        """ + verificationLink + """
                         ><b>Click here</b></a>
                        <br>
                        """)
                .build();
        resend.emails().send(sendEmailRequest);
    }
}
