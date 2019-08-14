package mbook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document(collection = "character")
public class GameCharacter {

    @Id
    private String id;
    
    private String name;
    
    private String ownerId;
    
}
