package mbook.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GameClass {
    private String name;
    private String specialization;
    private String favoredAttribute_1;
    private String favoredAttribute_2;
    private ArrayList<String> majorSkills;
    private ArrayList<String> minorSkills;
    
}
