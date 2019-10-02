package mbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mbook.user.User;
import mbook.user.UserService;

@Controller
public class changePasswordController extends AbstractWebController {
    @Autowired
    private UserService userService;
     
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePassword(Model model) {
        return "changePassword";
    } 
    
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String updatePassword(
            Model model,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword
    ) {
        User user = userService.findUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        
        if ( !userService.checkPassword(user, oldPassword)) {
            model.addAttribute("error", "Old password does not match");
        } else if ( oldPassword.equals(newPassword) ) {
            model.addAttribute("error", "New password can not match old password");
        } else {
            //Success
            userService.changePassword(user, newPassword);
            model.addAttribute("pwChanged", true);
        }
        return "changePassword";
    } 
}
