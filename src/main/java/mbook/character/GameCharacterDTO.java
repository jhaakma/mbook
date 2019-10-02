package mbook.character;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DTO of GameCharacter for fields that can be updated via the Patch API
 * @author Jeremy Haakma
 *
 */
@Getter 
@Setter
public class GameCharacterDTO {
    private String className;
    private String specialization;
    private String favoredAttribute_1;
    private String favoredAttribute_2;
    private Map<String, Integer> attributes;
    private Map<String, Integer> majorSkills;
    private Map<String, Integer> minorSkills;
    private Map<String, Integer> miscSkills;
    private String race;
    private String sex;
    private Integer gold;
    private Integer level;
    private String bio;
    private Integer deathCount = 0;
}
