package mbook.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mbook.role.Role;
import mbook.role.RoleRepository;
import mbook.user.verification.VerificationToken;
import mbook.user.verification.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }
    
    public User findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }
    
    public User findUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if ( user.isPresent() ) {
           return user.get(); 
        }
        return null;
    }
    
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userRepository.findAll();
    }
    
    public User createUser(User user, ArrayList<String> roles) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setEnabled(true);
        ArrayList<Role> userRoles = new ArrayList<Role>();
        for ( String role : roles ) {
            Role userRole = roleRepository.findByRole(role);
            userRoles.add(userRole);
        }

        user.setRoles(new HashSet<>(userRoles));
        return userRepository.save(user);
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    
    public User createUser(User user, String role) {
        return createUser(user, new ArrayList<String>(Arrays.asList(role)));
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
    

    public void saveCharacter( User user, String characterName ) {
        user.getGameCharacters().add(characterName);
        userRepository.save(user);
    }
    
    
    public void removeCharacter( User user, String characterName ) {
        user.getGameCharacters().remove( characterName);
        userRepository.save(user);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(username);
        if(user == null) {
            user = userRepository.findByUsernameIgnoreCase(username);
        }
        
        if(user != null && user.isEnabled() ) {
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }
    
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
}
