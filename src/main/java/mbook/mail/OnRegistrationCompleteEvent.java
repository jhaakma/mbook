package mbook.mail;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import mbook.user.User;

@Getter @Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private String appUrl;
    private Locale locale;
    private User user;
 
    public OnRegistrationCompleteEvent(
      User user, Locale locale, String appUrl) {
        super(user);
         
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}