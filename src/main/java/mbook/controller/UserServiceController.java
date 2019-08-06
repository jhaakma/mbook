package mbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mbook.model.User;
import mbook.service.UserService;

@RestController
public class UserServiceController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<Object> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(
            @RequestBody User user
    ) {
        userService.createUser(user);
        String responseMsg = "User " + user.getFullname() + " created successfully";
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(
            @PathVariable("username") String username
    ) {
        userService.deleteUser(username);
        String responseMsg = "User " + username + " deleted successfully";
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateUser( @RequestBody User user) {
        userService.updateUser(user);
        String responseMsg = "User " + user.getFullname() + " updated successfully";
        return new ResponseEntity<>(responseMsg, HttpStatus.OK);
    }
    
}
