package mbook.morrowind.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Race {
    ALTMER("Altmer"),
    ARGONIAN("Argonian"),
    BOSMER("Bosmer"),
    BRETON("Breton"),
    DUNMER("Dunmer"), 
    IMPERIAL("Imperial"),
    KHAJIIT("Khajiit"),
    NORD("Nord"),
    ORC("Orc"),
    REDGUARD("Redguard");
    
    @Getter private String value;
    public String toString() {
        return this.getValue();
    }
}
