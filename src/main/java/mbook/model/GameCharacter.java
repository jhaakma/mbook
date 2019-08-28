package mbook.model;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonMerge;

import lombok.Getter;
import lombok.Setter;
import mbook.morrowind.model.Attribute;
import mbook.morrowind.model.GameClass;
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
public class GameCharacter {
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
    
    private String profileImage;
    private String bio;
    private Integer deathCount;
    
}
