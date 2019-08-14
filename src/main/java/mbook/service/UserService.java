package mbook.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetailsService;

import mbook.model.GameCharacter;
import mbook.model.User;

public interface UserService extends UserDetailsService {

    public User findUserByEmail(String email);
    public User findUserByUsername(String username);
    public User findUserById(String id);
    public ArrayList<User> getUsers();
    public void createUser(User user, ArrayList<String> roles);
    public void createUser(User user, String role);
    public void deleteUser( String email );
    public void changePassword(User user, String password);
    public Boolean checkPassword(User user, String password);
    public void addCharacter( User user, GameCharacter character );
    public void removeCharacter( User user, GameCharacter character );
    
}
