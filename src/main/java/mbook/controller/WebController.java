package mbook.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mbook.model.User;
import mbook.model.Video;
import mbook.service.UserDetailsServiceImpl;
import mbook.service.VideoService;

@Controller
public class WebController {

    @Autowired
    Environment env;
    @Autowired
    VideoService videoService;
    //@Autowired
    //UserService userService;
    @Autowired
    private UserDetailsServiceImpl userService;
    
    @ModelAttribute
    public void populateModel(Model model) {
        model.addAttribute( "siteName", env.getProperty("custom.siteName") );
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if ( !model.containsAttribute("currentUser") ) {
            User user = userService.findUserByEmail(auth.getName());
            model.addAttribute("currentUser", user);
        }
        
        
    }
    
    @RequestMapping("/")
    public String home() {
        return "index";
    }
    
    
    
    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public String videos(HttpServletRequest request, Model model) {
        
        String id = request.getParameter("id");
        if ( id != null ) {
            Video vid = videoService.getVideo(id);
            if ( vid != null ) {
                ArrayList<Video> vids = new ArrayList<Video>();
                vids.add(vid);
                model.addAttribute("videos", vids);
            }
        } else {
            model.addAttribute("videos", videoService.getVideos() );
        }
        return "videos";
    }
        
    @RequestMapping(value = "/userlist", method = RequestMethod.GET)
    public String userlist(Model model) {    
        model.addAttribute("users", userService.getUsers());
        return "userlist";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }  
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String createNewUser( @Valid User user, Model model) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            model.addAttribute("errorMessage", "A user with that email already exists.");
            return "signup";
        } else {
            userService.saveUser(user);
            model.addAttribute("successMessage", "Registered. Successfully. You may now log in.");
            model.addAttribute("user", new User());
            return "login";
        }
    }
    
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        
        model.addAttribute("adminMessage", "Admins only");
        
        return "dashboard";
    }
}
