package mbook.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        @Valid @RequestBody GameCharacter gameCharacter
    )
    {
 
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if ( user == null ) {
            return new ResponseEntity<>("User is null", HttpStatus.FORBIDDEN);
        }
        GameCharacter existingCharacter = gameCharacterService.findCharacterByNameAndOwner(user, gameCharacter.getName());
        if ( existingCharacter != null ) {
            return new ResponseEntity<>("Character already exists", HttpStatus.BAD_REQUEST);
        }
        
        gameCharacter.setOwnerId(user.getId());
        gameCharacterService.saveCharacter(user, gameCharacter);
        String responseMsg = "Character " + gameCharacter.getName() + " saved successfully. ID: " + gameCharacter.getId(); 
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    @PostMapping("/api/updateCharacter")
    public ResponseEntity<Object> updateCharacter(
        @RequestBody GameCharacter gameCharacter
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if ( user == null ) {
            return new ResponseEntity<>("User is null", HttpStatus.FORBIDDEN);
        }
        
        if ( gameCharacter.getName() == null ) {
            return new ResponseEntity<>("No character name provided", HttpStatus.BAD_REQUEST);
        }
        GameCharacter existingCharacter = gameCharacterService.findCharacterByNameAndOwner(user, gameCharacter.getName());
        if ( existingCharacter == null ) {
            return new ResponseEntity<>("Character not found", HttpStatus.BAD_REQUEST);
        }
        gameCharacter.setOwnerId(user.getId());
        gameCharacter.setId(existingCharacter.getId() );
        gameCharacterService.saveCharacter(user, gameCharacter);
        
        String responseMsg = "Character " + gameCharacter.getName() + " updated successfully. ID: " + gameCharacter.getId();
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
    
    @PostMapping("/api/deleteCharacter")
    public ResponseEntity<Object> deleteCharacter(
        @RequestBody GameCharacter character
    ) {
        String characterName = character.getName();
        if ( characterName == null ) {
            return new ResponseEntity<>("Character name not provided", HttpStatus.BAD_REQUEST);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if ( user == null ) {
            return new ResponseEntity<>("User is null", HttpStatus.FORBIDDEN);
        }
        GameCharacter existingCharacter = gameCharacterService.findCharacterByNameAndOwner(user, characterName);
        if ( existingCharacter == null ) {
            return new ResponseEntity<>("Character not found", HttpStatus.BAD_REQUEST);
        }
        
        gameCharacterService.deleteCharacter(user, existingCharacter);
        String responseMsg = "Character " + characterName + " deleted successfully. ID: " + existingCharacter.getId();
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
    

    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
