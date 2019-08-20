package mbook.service;

import java.util.ArrayList;

import mbook.model.GameCharacter;
import mbook.model.User;

public interface GameCharacterService {
    public ArrayList<GameCharacter> getCharacters();
    public User getOwner(GameCharacter gameCharacter);
    public void saveCharacter(User user, GameCharacter gameCharacter);
    public void deleteCharacter(User user, GameCharacter gameCharacter);
    public GameCharacter findCharacterById(String id);
    public GameCharacter findCharacterByNameAndOwner(User owner, String name);
}
