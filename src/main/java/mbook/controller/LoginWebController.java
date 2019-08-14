package mbook.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mbook.model.Role;
import mbook.model.User;
import mbook.service.UserService;

/**
 * Controller for signing up and logging in.
 * @author jh1540
 *
 */
@Controller
public class LoginWebController extends AbstractWebController {
    @Autowired
    private UserService userService;
    
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
            @Valid User user, 
            BindingResult bindingResult,
            Model model
    ) {
        if ( bindingResult.hasErrors() ) {
            String errorMsg = String.format("Invalid %s: %s",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage()
            );
            model.addAttribute("errorMessage", errorMsg);// "Invalid value: " + bindingResult.getFieldError().getRejectedValue() ); 
            return "/signup";
        }
        
        User userEmailExists = userService.findUserByEmail(user.getEmail());
        User userUsernameExists = userService.findUserByUsername(user.getUsername());
        if (userEmailExists != null || userUsernameExists != null ) {
            if (userEmailExists != null) {
                model.addAttribute("errorMessage", "A user with that email already exists.");
                return "signup";
            } else {
                model.addAttribute("errorMessage", "A user with that username already exists.");
                return "signup";
            }
        } else {
            userService.createUser(user, Role.Type.USER.getValue() );
            model.addAttribute("successMessage", "Registered. Successfully. You may now log in.");
            model.addAttribute("user", new User());
            return "login";
        }
    }
    
}
