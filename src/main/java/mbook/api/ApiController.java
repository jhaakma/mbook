package mbook.api;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mbook.character.GameCharacter;
import mbook.character.GameCharacterDTO;
import mbook.character.GameCharacterService;
import mbook.user.User;
import mbook.user.UserService;

@Slf4j
@RestController
public class ApiController {

    @Autowired
    private GameCharacterService gameCharacterService;
    @Autowired
    private UserService userService;
    @Autowired
    private SmartValidator validator;

    private String createSuccessMessage(String name, String command) {
        return String.format("Character '%s' %s successfully", name, command);
    }

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByUsername(auth.getName());
    }

    
    
    //API Endpoints:

    @GetMapping("/api/user")
    public User getCurrentUser() {
        return getLoggedInUser();
    }

    @GetMapping("/api/characters")
    public List<GameCharacter> getCharacters() {
        return gameCharacterService.getCharacters();
    }

    @GetMapping("/api/characters/{name}")
    public ResponseEntity<Object> getMyCharacter(@PathVariable String name) {
        User user = getLoggedInUser();
        if (user == null) {
            //Really shouldn't happen
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        }
        GameCharacter thisCharacter = gameCharacterService.findCharacterByOwnerAndName(user, name);
        return new ResponseEntity<Object>(thisCharacter, HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/characters/{name}")
    public ResponseEntity<Object> createCharacter(
            @RequestBody GameCharacterDTO gameCharacterDTO, 
            @PathVariable String name,
            BindingResult result
    ) {
        User user = getLoggedInUser();
        if (user == null) {
            //Really shouldn't happen
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        }
        GameCharacter newCharacter = new GameCharacter();
        newCharacter.setName(name);
        newCharacter.setOwner(user.getUsername());
        BeanUtils.copyProperties(gameCharacterDTO, newCharacter);
        validator.validate(newCharacter, result);
        if  ( result.hasErrors() ) {
            return new ResponseEntity<>(result.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }
        gameCharacterService.saveCharacter(user, newCharacter);
        return new ResponseEntity<>(createSuccessMessage(newCharacter.getName(), "created"), HttpStatus.OK);
    }

    @DeleteMapping("/api/characters/{name}")
    public ResponseEntity<Object> deleteCharacter(@PathVariable String name) {
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

    @PatchMapping("/api/characters/{name}")
    public ResponseEntity<Object> updateGameCharacter(
            @RequestBody GameCharacterDTO gameCharacterDTO,
            @PathVariable String name,
            BindingResult result
    ) {
        //check user exists
        User user = getLoggedInUser();
        if (user == null) {
            //Really shouldn't happen
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        }
        //and character exists
        GameCharacter existingCharacter = gameCharacterService.findCharacterByOwnerAndName(user, name);
        if (existingCharacter == null) {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }
        //Transfer non-null data to existing character
        try {
            PropertyUtils.describe(gameCharacterDTO).entrySet().stream().filter(e -> e.getValue() != null)
                .filter(e -> !e.getKey().equals("class")).forEach(e -> {
                    try {
                        PropertyUtils.setProperty(existingCharacter, e.getKey(), e.getValue());
                    } catch (Exception exception) {
                     // TODO Auto-generated catch block
                        log.error("Our DTO and Entity are out of whack");
                        exception.printStackTrace();
                    }
                });
        } catch (Exception e ) {
            log.error("Something went wrong turning GameCharacterDTO into a GameCharacter.");
            e.printStackTrace();
            return new ResponseEntity<>("Failed to convert to GameCharacter object", HttpStatus.BAD_REQUEST);
        }
        //Validate result
        validator.validate(existingCharacter, result);
        if  ( result.hasErrors() ) {
            return new ResponseEntity<>(result.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }
        gameCharacterService.saveCharacter(user, existingCharacter);
        return new ResponseEntity<>(createSuccessMessage(name, "updated"), HttpStatus.OK);
    }

    
    @PutMapping("/api/characters/{characterName}/headshot")
    public ResponseEntity<Object> uploadHeadshot(
            @PathVariable String characterName
    ) { 
        // Get user
        User user = getLoggedInUser();
        if (user == null) {
            //Really shouldn't happen
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        }
        // Check character exists for user
        GameCharacter existingCharacter = gameCharacterService.findCharacterByOwnerAndName(user, characterName);
        if (existingCharacter == null) {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }
        // Save file to /images/{username_characterName}

        // Update Character with link to file 
        
        //Return success
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }
     

}
