package mbook.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mbook.model.GameCharacter;
import mbook.model.User;
import mbook.repository.GameCharacterRepository;

@Service
public class GameCharacterServiceImpl implements GameCharacterService {

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
    
    public void saveCharacter(User user, GameCharacter gameCharacter) { 
        GameCharacter savedCharacter = gameCharacterRepository.save(gameCharacter);
        if ( userService.findCharacter(user, savedCharacter) == null ) {
            userService.addCharacter(user, savedCharacter);
        }
    }
    
    public void deleteCharacter(User user, GameCharacter gameCharacter) {
        userService.removeCharacter(user, gameCharacter);
        gameCharacterRepository.delete(gameCharacter);
    }
    
    public GameCharacter findCharacterById(String id) {
        return gameCharacterRepository.findById(id).get();
    }
    
    public GameCharacter findCharacterByNameAndOwner(User owner, String name) {
        return gameCharacterRepository.findByOwnerAndName( owner.getId(), name);
    }
}
