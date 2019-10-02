package mbook.character;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import mbook.morrowind.model.Attribute;
import mbook.morrowind.model.Race;
import mbook.morrowind.model.Sex;
import mbook.morrowind.model.Skill;
import mbook.morrowind.model.Specialization;
import mbook.validation.EnumListValidator;
import mbook.validation.EnumValidator;

/**
 * Model representing Morrowind Player object
 * @author Jeremy Haakma
 *
 */
@Getter @Setter
@Document(collection = "gameCharacter")
@CompoundIndex(name="name_owner", def = "{'owner':1, 'name':1}", unique=true)
public class GameCharacter implements Comparable<GameCharacter> {
    @Id
    private String id;
    
    private String owner;
    
    @Indexed
    private String name;

    //Class
    private String className;
    
    @EnumValidator(enumClass=Specialization.class)
    private String specialization;
    
    @EnumValidator(enumClass=Attribute.class)
    private String favoredAttribute_1;
    
    @EnumValidator(enumClass=Attribute.class)
    private String favoredAttribute_2;
    
    @Size(min=8, max=8)
    @EnumListValidator(enumClass=Attribute.class)
    private Map<String, Integer> attributes;
    
    @Size(min=5, max=5)
    @EnumListValidator(enumClass=Skill.class)
    private Map<String, Integer> majorSkills;
    
    @Size(min=5, max=5)
    @EnumListValidator(enumClass=Skill.class)
    private Map<String, Integer> minorSkills;
    
    @EnumListValidator(enumClass=Skill.class)
    private Map<String, Integer> miscSkills;
    
    
    @NotNull @EnumValidator(enumClass=Race.class)
    private String race;
    
    @NotNull @EnumValidator(enumClass=Sex.class)
    private String sex;
    
    @NotNull
    private Integer gold;
    
    @NotNull
    private Integer level;
    
    
    private String profileImage = "images/default.png";
    private String bio;
    private Integer deathCount = 0;
    private Integer score = 0;
    

    @Override
    public int compareTo(GameCharacter arg0) {
        // TODO Auto-generated method stub
        return this.score - arg0.score;
    }
}
