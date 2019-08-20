package mbook.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

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
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/videos")
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
        
    @GetMapping("/userlist")
    public String userlist(Model model) {    
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
        Model model 
    ) {
        
        String id = request.getParameter("id");
        if ( id != null ) {
            GameCharacter gameCharacter = gameCharacterService.findCharacterById(id);
            String ownerId = gameCharacter.getOwnerId();
            User owner;
            if (ownerId != null ) {
                owner = userService.findUserById(ownerId);
                
            } else {
                owner = new User();
                owner.setUsername("(User doesn't exist");
                owner.setEmail("");
            }
            model.addAttribute("owner", owner);
            model.addAttribute("character", gameCharacter);

            return "character";
        }
        return "error";
                
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
            user = userService.findUserByEmail(auth.getName());
        }
        

        if ( user != null ) {
            model.addAttribute(user);
        }
        return "profile";
    }
    
    
}
