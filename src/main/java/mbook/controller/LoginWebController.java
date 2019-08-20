package mbook.controller;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mbook.event.OnRegistrationCompleteEvent;
import mbook.model.Role;
import mbook.model.User;
import mbook.model.VerificationToken;
import mbook.repository.VerificationTokenRepository;
import mbook.service.UserService;

/**
 * Controller for signing up and logging in.
 * @author jh1540
 *
 */
@Controller
public class LoginWebController extends AbstractWebController {
   
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
     
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }  
    

    @PostMapping("/signup")
    public String createNewUser( 
            HttpServletRequest request,
            @Valid User user, 
            BindingResult bindingResult,
            Model model
    ) {
        Locale locale = request.getLocale();
        
        //validation errors
        if ( bindingResult.hasErrors() ) {
            String errorMsg = String.format(messageSource.getMessage("signup.error", null, locale),
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage()
            );
            model.addAttribute("errorMessage", errorMsg);
            return "/signup";
        }
        
        //email exists
        User userEmailExists = userService.findUserByEmail(user.getEmail());
        if (userEmailExists != null) {
            model.addAttribute("errorMessage", messageSource.getMessage("signup.emailExists", null, locale) );
            return "signup";
        } 
        
        //user exists
        User userUsernameExists = userService.findUserByUsername(user.getUsername());
        if ( userUsernameExists != null ) {
            model.addAttribute("errorMessage", messageSource.getMessage("signup.userExists", null, locale) );
            return "signup";
        }
        
        //everything good, create user and send verification email
        User createdUser = userService.createUser(user, Role.Type.USER.getValue() );
        try {
            String appUrl = getURLBase(request);
            eventPublisher.publishEvent(
                new OnRegistrationCompleteEvent(createdUser, request.getLocale(), appUrl)
            );
        } catch (Exception me) {
            model.addAttribute("errorMessage", me.getMessage());
            return "signup";
        }
        
        model.addAttribute("successMessage", messageSource.getMessage("signup.success", null, locale));
        return "login";
        
    }
    
    @GetMapping("/confirmRegistration")
    public String confirmRegistration(
            WebRequest request, 
            Model model
    ) {
        Locale locale = request.getLocale();
        String token = request.getParameter("token");
      
         
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("errorMessage", messageSource.getMessage("signup.tokenInvalid", null, locale));
            return "login";
        }
         
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("errorMessage", messageSource.getMessage("signup.tokenExpired", null, locale));
            return "login";
        } 
         
        //Remove token from DB
        VerificationToken thisToken = verificationTokenRepository.findByUser(user);
        verificationTokenRepository.delete(thisToken);
        
        
        user.setEnabled(true); 
        userService.saveUser(user);
        model.addAttribute("successMessage",  messageSource.getMessage("signup.registrationSuccess", null, locale) );
        return "login"; 
    }
    
    
    private String getURLBase(HttpServletRequest request) throws MalformedURLException {
         return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        /*
         * URL requestURL = new URL(request.getRequestURL().toString()); String port =
         * requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort(); return
         * requestURL.getProtocol() + "://" + requestURL.getHost() + port;
         */

    }
    
}
