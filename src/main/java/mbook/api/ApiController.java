package mbook.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import mbook.model.GameCharacter;
import mbook.model.User;
import mbook.service.GameCharacterService;
import mbook.service.UserService;

@RestController
public class ApiController {

    @Autowired
    GameCharacterService gameCharacterService;
    @Autowired
    private UserService userService;
    
   
    @GetMapping("/api/users")
    public ResponseEntity<Object> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    
    //Create new game character
    @PostMapping("/api/createCharacter")
    public ResponseEntity<Object> createCharacter(
        @RequestBody GameCharacter gameCharacter
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if ( user == null ) {
            return new ResponseEntity<>("User is null", HttpStatus.FORBIDDEN);
        }
        gameCharacter.setOwnerId(user.getId());
        gameCharacterService.saveCharacter(gameCharacter);
        String responseMsg = "Character " + gameCharacter.getName() + "created successfully"; 
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
}
