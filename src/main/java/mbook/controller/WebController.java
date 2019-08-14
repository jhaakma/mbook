package mbook.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mbook.model.Video;
import mbook.service.UserService;
import mbook.service.VideoService;

@Controller
public class WebController extends AbstractWebController {

    @Autowired
    VideoService videoService;
    @Autowired
    private UserService userDetailsService;
    
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
        model.addAttribute("users", userDetailsService.getUsers());
        return "userlist";
    }
    
    
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        
        model.addAttribute("adminMessage", "Admins only");
        
        return "dashboard";
    }
}
