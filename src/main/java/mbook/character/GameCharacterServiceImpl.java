package mbook.character;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mbook.user.User;
import mbook.user.UserService;

@Service
public class GameCharacterServiceImpl implements GameCharacterService {

    @Autowired
    private GameCharacterRepository gameCharacterRepository;
    @Autowired
    private UserService userService;
    
    @Override
    public ArrayList<GameCharacter> getCharacters() {
        return (ArrayList<GameCharacter>) gameCharacterRepository.findAll();
    }
    
    @Override
    public User getOwner(GameCharacter characterRecord) {
        return userService.findUserByUsername(characterRecord.getOwner());
    }
    
    @Override
    public void saveCharacter(User user, GameCharacter gameCharacter) { 
        GameCharacter savedCharacter = gameCharacterRepository.save(gameCharacter);
        userService.saveCharacter(user, savedCharacter.getName());
    }
    
    @Override
    @Transactional
    public void deleteCharacter(User user, String name) {
        userService.removeCharacter(user, name);
        GameCharacter characterToDelete = findCharacterByOwnerAndName(user, name);
        gameCharacterRepository.delete(characterToDelete);
    }
    
    @Override
    public GameCharacter findCharacterById(String id) {
        return gameCharacterRepository.findById(id).get();
    }
    
    @Override
    public GameCharacter findCharacterByOwnerAndName(User owner, String name) {
        return gameCharacterRepository.findByOwnerAndName(owner.getUsername(), name);
    }
    
    
    public ArrayList<GameCharacter> getTopScoringCharacters(Integer num) {
        PageRequest request = PageRequest.of(0, num, Sort.by("score").descending());
        return new ArrayList<GameCharacter>(gameCharacterRepository.findAll(request).getContent() );
    }
}
