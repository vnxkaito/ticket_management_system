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

    public void sendPasswordResetEmail(String to, String resetLink) {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("noreply@resend.dev")
                .to(to)
                .subject("Reset your password")
                .html("""
                        You requested a password reset for your TMS account.<br>
                        <br>
                        Click the link below to reset your password. This link expires in 1 hour.<br>
                        <br><a href=
                        """ + resetLink + """
                         ><b>Reset Password</b></a>
                        <br>
                        If you did not request this, please ignore this email.
                        """)
                .build();
        resend.emails().send(sendEmailRequest);
    }
}
