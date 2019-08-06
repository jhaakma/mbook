package mbook.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mbook.model.User;
import mbook.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;
    
    public ArrayList<User> getUsers() {
        return (ArrayList<User>) repository.findAll();
    }
    public User getUser( String email ) {
        return repository.findByEmail(email);
    }
    public void updateUser( User user ) {
        repository.save(user);
    }
    public void createUser( User user ) {
        repository.save(user);
    }
    public void deleteUser( String email ) {
        repository.delete(repository.findByEmail(email));
    }
}
