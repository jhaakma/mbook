package mbook.service;

import java.util.ArrayList;

import mbook.model.User;
import mbook.morrowind.model.CharacterRecord;

public interface CharacterRecordService {
    public ArrayList<CharacterRecord> getCharacters();
    public User getOwner(CharacterRecord characterRecord);
    public void saveCharacter(User user, CharacterRecord characterRecord);
    public void deleteCharacter(User user, String characterId );
    public CharacterRecord findCharacterById(String id);
    public CharacterRecord findCharacterByOwnerAndName(User owner, String name);
}
