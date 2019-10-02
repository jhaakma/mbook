package mbook.character;

import java.util.ArrayList;

import mbook.user.User;

public interface GameCharacterService {
    public ArrayList<GameCharacter> getCharacters();
    public User getOwner(GameCharacter gameCharacter);
    public void saveCharacter(User user, GameCharacter gameCharacter);
    public void deleteCharacter(User user, String characterId );
    public GameCharacter findCharacterById(String id);
    public GameCharacter findCharacterByOwnerAndName(User owner, String name);
    public ArrayList<GameCharacter> getTopScoringCharacters(Integer num);
}
