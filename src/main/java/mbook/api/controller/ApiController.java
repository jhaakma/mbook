package mbook.api.controller;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
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

import mbook.model.GameCharacter;
import mbook.model.User;
import mbook.morrowind.model.CharacterRecord;
import mbook.service.CharacterRecordService;
import mbook.service.UserService;

@RestController
public class ApiController {

    @Autowired
    CharacterRecordService characterRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    
   
    @GetMapping("/api/characters")
    public List<CharacterRecord> getCharacters() {
        return characterRecordService.getCharacters();
    }
    
    @GetMapping("/api/characters/{name}")
    public CharacterRecord getMyCharacter(
            @PathVariable String name
    ) {
        User user = getLoggedInUser();
        
        CharacterRecord thisCharacter = characterRecordService.findCharacterByOwnerAndName(user, name);
        return thisCharacter;
    }
    
    //Create new game character
    @PostMapping("/api/characters")
    public ResponseEntity<Object> createCharacter(
        @Valid @RequestBody CharacterRecord characterRecord
    )
    {
        User user = getLoggedInUser();
        if ( user == null ) {
            return new ResponseEntity<>("No Character", HttpStatus.BAD_REQUEST);
        }
        GameCharacter newGameCharacter = characterRecord.getGameCharacter();
        if ( newGameCharacter == null ) {
            return new ResponseEntity<>("Must include character data", HttpStatus.BAD_REQUEST);
        }
        CharacterRecord existingCharacter = characterRecordService.findCharacterByOwnerAndName(user, newGameCharacter.getName());
        if ( existingCharacter != null ) {
            return new ResponseEntity<>("Character already exists", HttpStatus.BAD_REQUEST);
        }
        
        characterRecord.setOwner(user.getUsername());
        characterRecordService.saveCharacter(user, characterRecord);
        return new ResponseEntity<>(createSuccessMessage(newGameCharacter.getName(), "created"), HttpStatus.OK);
    }
    
    @DeleteMapping("/api/characters/{name}")
    public ResponseEntity<Object> deleteCharacter(
            @PathVariable String name
    ) {
        User user = getLoggedInUser();
        if (user == null) {
            return new ResponseEntity<>("User is null", HttpStatus.BAD_REQUEST);
        }
        CharacterRecord existingCharacter = characterRecordService.findCharacterByOwnerAndName(user, name);
        if (existingCharacter == null) {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }

        characterRecordService.deleteCharacter(user, name);
        
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
        
        CharacterRecord existingCharacter = 
                characterRecordService.findCharacterByOwnerAndName(user, name);
        if ( existingCharacter == null ) {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }

        if ( !name.equals(existingCharacter.getGameCharacter().getName())) {
            return new ResponseEntity<>("Character Name does not match", HttpStatus.BAD_REQUEST);
        }
        gameCharacter.setName(name);
        existingCharacter.setOwner(user.getUsername());
        existingCharacter.setGameCharacter(gameCharacter);
        characterRecordService.saveCharacter(user, existingCharacter);
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
        CharacterRecord existingCharacter = 
                characterRecordService.findCharacterByOwnerAndName(user, name);
        try {
            updates.forEach((k, v) -> {
                // use reflection to get field k on manager and set it to value k
                 Field field = ReflectionUtils.findField(CharacterRecord.class, k);
                 if ( field.getName().equals("gameCharacter") ) {
                     GameCharacter newCharacter = objectMapper.convertValue(v, GameCharacter.class);
                     newCharacter.setName(name);
                     existingCharacter.setGameCharacter(newCharacter); 
                     
                 } else {
                     ReflectionUtils.makeAccessible(field);
                     ReflectionUtils.setField(field, existingCharacter, v);
                 }
             });
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
        
        characterRecordService.saveCharacter(user, existingCharacter);
        return new ResponseEntity<>(createSuccessMessage(name, "patched"), HttpStatus.OK);
    }
    
    
    
    
    private String createSuccessMessage(String name, String command) {
        return String.format(
            "Character '%s' %s successfully",
            name, command
        ); 
    }
    
    
    
    
    
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(auth.getName());
    }
}
