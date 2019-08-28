package mbook.morrowind.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Sex {
    MALE("Male"),
    FEMALE("Female");
    
    @Getter private String value;
    public String toString() {
        return this.getValue();
    }
}
