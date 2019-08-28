package mbook.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mbook.model.GameCharacter;
import mbook.model.User;
import mbook.morrowind.model.Skill;
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
    
    public User getOwner(GameCharacter characterRecord) {
        return userService.findUserByUsername(characterRecord.getOwner());
    }
    
    public void saveCharacter(User user, GameCharacter gameCharacter) { 
        GameCharacter savedCharacter = gameCharacterRepository.save(gameCharacter);
        userService.saveCharacter(user, savedCharacter.getName());
    }
    
    public void deleteCharacter(User user, String name) {
        userService.removeCharacter(user, name);
        GameCharacter characterToDelete = findCharacterByOwnerAndName(user, name);
        gameCharacterRepository.delete(characterToDelete);
    }
    
    public GameCharacter findCharacterById(String id) {
        return gameCharacterRepository.findById(id).get();
    }
    
    public GameCharacter findCharacterByOwnerAndName(User owner, String name) {
        return gameCharacterRepository.findByOwnerAndName(owner.getUsername(), name);
    }
}
