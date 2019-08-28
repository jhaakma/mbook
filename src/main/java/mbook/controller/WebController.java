package mbook.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.ServletContextResource;

import mbook.model.GameCharacter;
import mbook.model.User;
import mbook.model.Video;
import mbook.service.GameCharacterService;
import mbook.service.UserService;
import mbook.service.VideoService;

@Controller
public class WebController extends AbstractWebController {

    @Autowired
    VideoService videoService;
    
    @Autowired
    GameCharacterService gameCharacterService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    ResourceLoader resourceLoader;
    
    /*
     * @Autowired ServletContext servletContext;
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/videos")
    public String videos(HttpServletRequest request, Model model) {
        
        String id = request.getParameter("id");
        if ( id != null ) {
            Video vid = videoService.getVideo(id);
                model.addAttribute("video", vid);
        } else {
            
        }
        model.addAttribute("videos", videoService.getVideos() );
        return "videos";
    }
        
    @GetMapping("/userlist")
    public String userlist(
        HttpServletRequest request, 
        Model model  
    ) {    
        String username = request.getParameter("user");
        if ( username != null ) {
            model.addAttribute("user", userService.findUserByUsername(username));
        }
        model.addAttribute("users", userService.getUsers());
        return "userlist";
    }
    
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        
        model.addAttribute("adminMessage", "Admins only");
        
        return "dashboard";
    }
    
    @GetMapping("/character")
    public String character(
        WebRequest request,
        Model model,
        @RequestParam(value="character") String characterName,
        @RequestParam(value="user") String username
    ) {

        User owner = userService.findUserByUsername(username);
        if ( owner == null ) {
            return "error";
        }
        GameCharacter gameCharacter = gameCharacterService.findCharacterByOwnerAndName(owner, characterName);
        if ( gameCharacter == null ) {
            return "error";
        }
        model.addAttribute("owner", owner);
        model.addAttribute("character", gameCharacter);
        return "characterProfile";
    }
    
    @GetMapping("/profile")
    public String profile(
        WebRequest request, 
        Model model
    ) {
        String username = request.getParameter("username");
        User user;
        if ( username != null ) {
            user = userService.findUserByUsername(username);
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            user = userService.findUserByUsername(auth.getName());
        }
        

        if ( user != null ) {
            model.addAttribute("user", user);
        }
        return "userProfile";
    }
    

    
    
    
    
}
