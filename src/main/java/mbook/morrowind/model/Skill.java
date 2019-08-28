package mbook.morrowind.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Skill {
    HEAVY_AMOR("Heavy Armor"),
    MEDIUM_ARMOR("Medium Armor"),
    SPEAR("Spear"),
    ACROBATICS("Acrobatics"),
    ARMORER("Armorer"),
    AXE("Axe"),
    BLUNT_WEAPON("Blunt Weapon"),
    LONG_BLADE("Long Blade"),
    BLOCK("Block"),
    LIGHT_ARMOR("Light Armor"),
    MARKSMAN("Marksman"),
    SNEAK("Sneak"),
    ATHLETICS("Athletics"),
    HAND_TO_HAND("Hand-to-hand"),
    SHORT_BLADE("Short Blade"),
    UNARMORED("Unarmored"),
    ILLUSION("Illusion"),
    MERCANTILE("Mercantile"),
    SPEECHCRAFT("Speechcraft"),
    ALCHEMY("Alchemy"),
    CONJURATION("Conjuration"),
    ENCHANT("Enchant"),
    SECURITY("Security"),
    ALTERATION("Alteration"),
    DESTRUCTION("Destruction"),
    MYSTICISM("Mysticism"),
    RESTORATION("Restoration");
    
    @Getter private String value;
    public String toString() {
        return this.getValue();
    }
}
