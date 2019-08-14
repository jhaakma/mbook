package mbook.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mbook.validation.UsernameConstraint;


@Getter @Setter
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true) 
    @NotNull 
    @Size(min=3, max=12, message="must be between 3 and 12 characters.")
    @UsernameConstraint
    private String username;
    
    @Indexed(unique = true, direction = IndexDirection.DESCENDING )
    @NotBlank(message="no email provided.")
    private String email;
    
    @NotNull
    @Size(min=7, max=50, message="must be between 7 and 50 characters.")
    private String password;
    
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
    @DBRef(lazy = true)
    private Set<GameCharacter> gameCharacters = new HashSet<>();
    
    @Override
    public String toString() {
        return String.format(
            "Customer[id=%s, username='%s' email='%s']",
            id, username, email);
    }
}
