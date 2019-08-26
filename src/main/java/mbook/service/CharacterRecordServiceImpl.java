package mbook.service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mbook.model.User;
import mbook.morrowind.model.CharacterRecord;
import mbook.repository.GameCharacterRepository;

@Service
public class CharacterRecordServiceImpl implements CharacterRecordService {

    @Autowired
    private GameCharacterRepository gameCharacterRepository;
    @Autowired
    private UserService userService;
    
    public ArrayList<CharacterRecord> getCharacters() {
        return (ArrayList<CharacterRecord>) gameCharacterRepository.findAll();
    }
    
    public User getOwner(CharacterRecord characterRecord) {
        return userService.findUserByUsername(characterRecord.getOwner());
    }
    
    public void saveCharacter(User user, CharacterRecord characterRecord) { 
        CharacterRecord savedCharacter = gameCharacterRepository.save(characterRecord);
        userService.saveCharacter(user, savedCharacter.getGameCharacter().getName());
    }
   
    
    public void deleteCharacter(User user, String name) {
        userService.removeCharacter(user, name);
        CharacterRecord characterToDelete = findCharacterByOwnerAndName(user, name);
        gameCharacterRepository.delete(characterToDelete);
    }
    
    public CharacterRecord findCharacterById(String id) {
        return gameCharacterRepository.findById(id).get();
    }
    
    public CharacterRecord findCharacterByOwnerAndName(User owner, String name) {
        return gameCharacterRepository.findByOwnerAndName( owner.getUsername(), name);
    }
}
