package vttp.miniproj.App_Server.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import vttp.miniproj.App_Server.Repository.UserRepository;
import vttp.miniproj.App_Server.models.UserData;

@Service
public class RegistrationService {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;

    public void insertUserData(UserData userData) {

        String uuid = UUID.randomUUID().toString().substring(0,8);
        userData.setUserId(uuid);

        String token = UUID.randomUUID().toString();
        userData.setVerificationToken(token);
        userData.setVerified(false);

        userRepo.insertUserData(userData);
        sendConfirmationEmail(userData.getEmail(), token);
    }

    public boolean checkEmailExist(String email){
        
        return userRepo.checkEmailExist(email);
    }

    private void sendConfirmationEmail(String toEmail, String token) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String verificationUrl = "http://localhost:4200/verify?token=" + token;

        String htmlContent = "<p>Please click the button below to verify your email:</p>"
            + "<a href=\"" + verificationUrl + "\" style=\""
            + "display: inline-block; padding: 10px 20px; color: white; background-color: #4CAF50; "
            + "text-decoration: none; border-radius: 5px;\">Verify Email</a>"
            + "<p>If the button doesn't work, you can also click this link: <br><a href=\"" + verificationUrl + "\">" + verificationUrl + "</a></p>";

        helper.setTo(toEmail);
        helper.setSubject("Please confirm your email");
        helper.setText(htmlContent, true);
        helper.setFrom("your_email@gmail.com");

        mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUserByToken(String token) {
        return userRepo.verifyUserByToken(token);
    }

    public boolean checkUsernameExist(String username) {
        return userRepo.checkUsernameExist(username);
    }

}
