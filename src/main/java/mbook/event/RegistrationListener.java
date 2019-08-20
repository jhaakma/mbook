package mbook.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import mbook.model.User;
import mbook.service.MailContentBuilder;
import mbook.service.UserService;

@Component
public class RegistrationListener implements
  ApplicationListener<OnRegistrationCompleteEvent> {
  
    @Autowired
    private UserService service;
  
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private MailContentBuilder mailContentBuilder;
    
    @Autowired
    Environment env;
 
    @Override    
    @Async
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {

            try {
                this.confirmRegistration(event);
            } catch (AddressException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
 
    /**
     * Send an email for account verification
     * @param event
     * @throws MessagingException 
     * @throws AddressException 
     */
    private void confirmRegistration(OnRegistrationCompleteEvent event) throws AddressException, MessagingException {
        
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);
        
        String recipientAddress;
        if ( Boolean.parseBoolean(env.getProperty("custom.testMode") ) == true ) {
            recipientAddress = env.getProperty("custom.mail.testRecipient");
        } else {
            recipientAddress = user.getEmail();
        }
        String subject = messageSource.getMessage("signup.email.subject", null, event.getLocale());
        String username = user.getUsername();
        String confirmationUrl = event.getAppUrl() + "/confirmRegistration?token=" + token;
        
        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("username", username);
        templateVariables.put("url", confirmationUrl);
        String content = mailContentBuilder.build("registrationEmail", templateVariables);
        
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email);
        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(email);
    }
}