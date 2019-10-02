package mbook.role;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document(collection = "role")
public class Role {
    
    @AllArgsConstructor
    public enum Type {
        USER("USER"),
        ADMIN("ADMIN");

        @Getter private String value;
    }
    
    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING )
    private String role;
}
