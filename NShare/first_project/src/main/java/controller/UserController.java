package controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storageModel.StorageBuilder;
import dataModel.User;
import storageModel.UserStorage;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by banhidi on 5/21/2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @RequestMapping(method = GET, path = "/authentify")
    public ResponseEntity<dataModel.User> getUser(@RequestParam(value = "username") String username,
                                                  @RequestParam(value = "password") String password) {
        try {
            dataModel.User u = StorageBuilder.getUserStorage().getUser(username, password);
            if (u == null)
                return new ResponseEntity(
                        "Username or password is incorrect.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity<dataModel.User>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = GET, path="/regular")
    public ResponseEntity getUsersByType() {
        try {
            List<User> regularUsers = StorageBuilder.getUserStorage().getAllUsers("regular");
            return new ResponseEntity<List<User>>(regularUsers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<User>> getUser() {
        try {
            List<User> usersList = StorageBuilder.getUserStorage().getAllUsers();
            return new ResponseEntity(usersList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = PUT)
    public ResponseEntity putUser(@RequestBody User user) {
        try {
            UserStorage userStorage = StorageBuilder.getUserStorage();
            if (userStorage.getUser(user.getUsername()) != null)
                return new ResponseEntity(
                        "This username already exists.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            else {
                userStorage.insertUser(user);
                return new ResponseEntity<Boolean>(HttpStatus.OK);
            }
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = DELETE)
    public ResponseEntity deleteUser(@RequestParam(value="id") int id) {
        try {
            StorageBuilder.getUserStorage().deleteUser(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = POST)
    public ResponseEntity modifyUser(@RequestBody User u) {
        try {
            UserStorage userStorage = StorageBuilder.getUserStorage();
            User existingUser = userStorage.getUser(u.getUsername());
            if (existingUser != null) {
                if (existingUser.getId() == u.getId()) {
                    userStorage.modifyUser(u);
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(
                            "This username is already in use.",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }
            } else {
                userStorage.modifyUser(u);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
