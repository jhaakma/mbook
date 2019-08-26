package mbook.morrowind.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonMerge;

import lombok.Getter;
import lombok.Setter;
import mbook.model.GameCharacter;

/**
 * Model representing Morrowind Player object
 * @author Jeremy Haakma
 *
 */
@Getter @Setter
@Document(collection = "character-record")
@CompoundIndex(name="name_owner", def = "{'owner':1, 'gameCharacter.name':1}", unique=true)
public class CharacterRecord {
    @Id
    private String id;
    private String owner;
    @JsonMerge
    private GameCharacter gameCharacter;
    private String profilePic;
    private String bio;
    private Integer deathCount;
    
}
