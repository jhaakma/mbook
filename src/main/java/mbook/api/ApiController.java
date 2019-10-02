package mbook.api;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import mbook.character.GameCharacter;
import mbook.character.GameCharacterService;
import mbook.user.User;
import mbook.user.UserService;

@RestController
public class ApiController {

    

    
    @Autowired
    GameCharacterService gameCharacterService;
    @Autowired
    private UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    
    private String createSuccessMessage(String name, String command) {
        return String.format(
            "Character '%s' %s successfully",
            name, command
        ); 
    }
    
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByUsername(auth.getName());
    }
    
    //endpoints
    
    @GetMapping("/api/user")
    public User getCurrentUser() {
        return getLoggedInUser();
    }
   
    @GetMapping("/api/characters")
    public List<GameCharacter> getCharacters() {
        return gameCharacterService.getCharacters();
    }
    
    @GetMapping("/api/characters/{name}")
    public ResponseEntity<Object> getMyCharacter(
            @PathVariable String name
    ) {
        User user = getLoggedInUser();
        if ( user != null) {
            GameCharacter thisCharacter = gameCharacterService.findCharacterByOwnerAndName(user, name);
            return new ResponseEntity<Object>(thisCharacter, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<Object>("User not found", HttpStatus.BAD_REQUEST);
        }
    }
    
    //Create new game character
    @PostMapping("/api/characters")
    public ResponseEntity<Object> createCharacter(
        @Valid @RequestBody GameCharacter gameCharacter
    ) {
        User user = getLoggedInUser();
        if ( user == null ) {
            return new ResponseEntity<>("No Character", HttpStatus.BAD_REQUEST);
        }
        gameCharacter.setOwner(user.getUsername());
        gameCharacterService.saveCharacter(user, gameCharacter);
        return new ResponseEntity<>(createSuccessMessage(gameCharacter.getName(), "created"), HttpStatus.OK);
    }
    
    @DeleteMapping("/api/characters/{name}")
    public ResponseEntity<Object> deleteCharacter(
            @PathVariable String name
    ) {
        User user = getLoggedInUser();
        if (user == null) {
            return new ResponseEntity<>("User is null", HttpStatus.BAD_REQUEST);
        }
        GameCharacter existingCharacter = gameCharacterService.findCharacterByOwnerAndName(user, name);
        if (existingCharacter == null) {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }
 
        gameCharacterService.deleteCharacter(user, name);
        
        return new ResponseEntity<>(createSuccessMessage(name, "deleted"), HttpStatus.OK);
    }
    
    /**
     * Update the entire GameCharacter object
     * @param gameCharacter
     * @param name
     * @return
     */
    @PutMapping("/api/characters/{name}")
    public ResponseEntity<Object> updateGameCharacter(
        @Valid @RequestBody GameCharacter gameCharacter,
        @PathVariable String name
    ) {
        User user = getLoggedInUser();
        
        GameCharacter existingCharacter = 
                gameCharacterService.findCharacterByOwnerAndName(user, name);
        if ( existingCharacter == null ) {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }

        if ( !name.equals(existingCharacter.getName())) {
            return new ResponseEntity<>("Character Name does not match", HttpStatus.BAD_REQUEST);
        }
        gameCharacter.setName(name);
        existingCharacter.setOwner(user.getUsername());
        gameCharacterService.saveCharacter(user, existingCharacter);
        return new ResponseEntity<>(createSuccessMessage(name, "updated"), HttpStatus.OK);
    }
    
    /**
     * Patch Character record fields (not including GameCharacter, which has its own PUT )
     * @return
     */
    @PatchMapping("/api/characters/{name}")
    public ResponseEntity<Object> patchCharacter(
            @RequestBody Map<String, Object> updates,
            @PathVariable String name
    ) { 
        User user = getLoggedInUser();
        GameCharacter existingCharacter = 
                gameCharacterService.findCharacterByOwnerAndName(user, name);
        try {
            updates.forEach((k, v) -> {
                // use reflection to get field k on manager and set it to value k
                 Field field = ReflectionUtils.findField(GameCharacter.class, k);
                 ReflectionUtils.makeAccessible(field);
                 ReflectionUtils.setField(field, existingCharacter, v);
             });
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        
        gameCharacterService.saveCharacter(user, existingCharacter);
        return new ResponseEntity<>(createSuccessMessage(name, "patched"), HttpStatus.OK);
    }
    
    @PutMapping("/api/characters/{name}/headshot")
    public ResponseEntity<Object> uploadHeadshot(
    ) {
        //Get user
        
        //Check character exists for user
        
        //Save file to /images/{username_characterName}
        
        //Update Character with link to file
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
