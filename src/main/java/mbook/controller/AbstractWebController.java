package mbook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import mbook.model.User;
import mbook.service.PageService;
import mbook.service.UserService;

/**
 * Contains shared logic for web controllers.
 * @author jh1540
 *
 */
public abstract class AbstractWebController {
    @Autowired
    Environment env;
    @Autowired
    private UserService userService;
    @Autowired 
    private PageService pageService;
    
    @ModelAttribute
    public void populateModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(auth.getAuthorities());
        
        if ( !(auth instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("loggedIn", true);
        }
        model.addAttribute( "pages", pageService.getAuthorisedPages(authorities) );
        
        if ( !model.containsAttribute("currentUser") ) {
            User user = userService.findUserByUsername(auth.getName());
            model.addAttribute("currentUser", user);
        }
    }
    

}
