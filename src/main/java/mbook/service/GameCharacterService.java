package mbook.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mbook.model.GameCharacter;
import mbook.model.User;
import mbook.repository.GameCharacterRepository;
@Service
public class GameCharacterService {

    @Autowired
    private GameCharacterRepository gameCharacterRepository;
    @Autowired
    private UserService userService;
    
    public ArrayList<GameCharacter> getCharacters() {
        return (ArrayList<GameCharacter>) gameCharacterRepository.findAll();
    }
    
    public User getOwner(GameCharacter gameCharacter) {
        return userService.findUserById(gameCharacter.getOwnerId());
    }
    
    
    public void saveCharacter(GameCharacter gameCharacter) {
        User thisUser = userService.findUserById(gameCharacter.getOwnerId());
        GameCharacter savedCharacter = gameCharacterRepository.save(gameCharacter);
        userService.addCharacter(thisUser, savedCharacter);
    }
}
