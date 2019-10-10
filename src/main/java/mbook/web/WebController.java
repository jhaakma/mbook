package mbook.web;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import mbook.character.GameCharacter;
import mbook.character.GameCharacterService;
import mbook.user.User;
import mbook.user.UserService;
import mbook.video.Video;
import mbook.video.VideoService;

@Slf4j
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
    public String home(  Model model  ) {
        model.addAttribute("list", gameCharacterService.getTopScoringCharacters(10));
        model.addAttribute("users", userService.getUsers());
        return "index";
    }
    
    @GetMapping("/videos")
    public String videos(HttpServletRequest request, Model model) {
        
        String id = request.getParameter("id");
        if ( id != null ) {
            Video vid = videoService.getVideo(id);
                model.addAttribute("video", vid);
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
    
    @GetMapping("/upload/{characterName}")
    public String test(
            @PathVariable String characterName,
            Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        GameCharacter gameCharacter = gameCharacterService.findCharacterByOwnerAndName(user, characterName);
        
        if ( gameCharacter != null ) {
            model.addAttribute("characterName", gameCharacter.getName());
            return "test";
        } else {
            log.info("Non game Character");
        }
        return "index";
        
    }
}
