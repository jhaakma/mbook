package mbook.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetailsService;

import mbook.model.User;
import mbook.model.VerificationToken;

public interface UserService extends UserDetailsService {
    public User findUserByEmail(String email);
    public User findUserByUsername(String username);
    public User findUserById(String id);
    public ArrayList<User> getUsers();
    public User createUser(User user, ArrayList<String> roles);
    public User createUser(User user, String role);
    public User saveUser(User user);
    public void deleteUser( String email );
    public void changePassword(User user, String password);
    public Boolean checkPassword(User user, String password);
    public void saveCharacter( User user, String characterName  );
    public void removeCharacter( User user, String characterName );
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
}
