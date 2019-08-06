package mbook.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING )
    
    private String email;
    
    private String password;
    private String fullname;
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
    
    
    @Override
    public String toString() {
        return String.format(
            "Customer[id=%s, username='%s' email='%s']",
            id, fullname, email);
    }
}
