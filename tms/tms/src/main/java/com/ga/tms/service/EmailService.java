package com.ga.tms.service;
import com.resend.*;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    Resend resend = new Resend("re_RjcTSRQq_HYfdaCLWoRV7v5EGHcbHcWXV");

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
        SendEmailResponse data = resend.emails().send(sendEmailRequest);
    }
//    public static void main(String[] args) {
//
//
//        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
//                .from("noreply@resend.dev")
//                .to("vnxkaito@hotmail.com")
//                .subject("Hello World")
//                .html("<p>Congrats on sending your <strong>first email</strong>!</p>")
//                .build();
//
//        SendEmailResponse data = resend.emails().send(sendEmailRequest);
//    }
}
