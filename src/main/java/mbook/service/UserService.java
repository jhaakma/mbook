package mbook.service;

import java.util.ArrayList;

import mbook.model.User;

public interface UserService {
    public abstract ArrayList<User> getUsers();
    public abstract User getUser(String username);
    public abstract void updateUser( User user );
    public abstract void createUser( User user );
    public abstract void deleteUser( String username );
}
