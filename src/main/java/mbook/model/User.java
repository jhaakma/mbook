package mbook.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import mbook.validation.UsernameConstraint;

@Getter
@Setter
//@NoArgsConstructor
@Document(collection = "user")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull
    @Size(min = 3, max = 20, message = "{user.username-size}")
    @UsernameConstraint
    private String username;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @NotBlank(message = "{user.noEmail}")
    private String email;

    @NotNull
    @Size(min = 4, max = 50, message = "{user.password-size}")
    private String password;

    private boolean enabled = false;
    @DBRef
    private Set<Role> roles;
    private Set<String> gameCharacters = new HashSet<>();

    
    public User() { 
        super(); 
        this.enabled = false; 
    }
     

    @Override
    public String toString() {
        return String.format("Customer[id=%s, username='%s' email='%s']", id, username, email);
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        this.getRoles().forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
}
