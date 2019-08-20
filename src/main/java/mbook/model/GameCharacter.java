package mbook.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document(collection = "character")
@CompoundIndex(name="name_owner", def = "{'ownerId':1, 'name':1}", unique=true)
public class GameCharacter {

    @Id 
    private String id;
    
    @NotNull
    private String name;
    @NotNull
    private GameClass gameClass;
    @NotNull
    private Integer gold;

    @NotNull
    private Integer level;
    private String ownerId;

    /*
     * @Override public int hashCode() { return id.hashCode(); }
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameCharacter) {
            GameCharacter thatCharacter = (GameCharacter) obj;
            return this.id.equals(thatCharacter.id);
        }
        return false;
    }
    
    public Boolean isNewCharacterValid() {
        return ( 
                this.getName() != null
        );
    }
}
