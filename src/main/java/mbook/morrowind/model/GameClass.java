package mbook.morrowind.model;

import java.util.ArrayList;
import java.util.Map;

import javax.validation.constraints.Size;

import lombok.Data;
import mbook.validation.EnumListValidator;
import mbook.validation.EnumValidator;

@Data
public class GameClass {
    private String name;
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
    
    @Size(min=17)
    @EnumListValidator(enumClass=Skill.class)
    private Map<String, Integer> miscSkills;
    
}