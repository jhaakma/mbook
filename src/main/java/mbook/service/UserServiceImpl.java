package mbook.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import mbook.model.GameCharacter;
import mbook.model.Role;
import mbook.model.User;
import mbook.repository.RoleRepository;
import mbook.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
 
    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
    
    public User findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }
    
    public User findUserById(String id) {
        return userRepository.findById(id).get();
    }
    
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userRepository.findAll();
    }
    
    public void createUser(User user, ArrayList<String> roles) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        ArrayList<Role> userRoles = new ArrayList<Role>();
        for ( String role : roles ) {
            Role userRole = roleRepository.findByRole(role);
            userRoles.add(userRole);
        }

        user.setRoles(new HashSet<>(userRoles));
        userRepository.save(user);
    }
    
    public void createUser(User user, String role) {
        createUser(user, new ArrayList<String>(Arrays.asList(role)));
    }
    
    public void deleteUser( String email ) {
        userRepository.delete(userRepository.findByEmailIgnoreCase(email));
    }
    
    
    public void changePassword(User user, String password) {
        if ( password != null ) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
        }
        userRepository.save(user);
    }
    
    public Boolean checkPassword(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }
    

    public void addCharacter( User user, GameCharacter character ) {
        user.getGameCharacters().add(character);
        userRepository.save(user);
    }
    
    public void removeCharacter( User user, GameCharacter character ) {
        user.getGameCharacters().remove(character);
        userRepository.save(user);
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(username);
        if(user == null) {
            user = userRepository.findByUsernameIgnoreCase(username);
        }
        
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }
    
    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }
    
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
