package controller;

import dataModel.*;
import dataModel.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import storageModel.StorageBuilder;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by banhidi on 5/21/2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @RequestMapping(method = GET)
    public ResponseEntity<dataModel.User> getUser(@RequestParam(value="username") String username,
                                        @RequestParam(value="password") String password) {
        dataModel.User u = StorageBuilder.getUserStorage().getUser(username, password);
        if (u == null)
            return new ResponseEntity<dataModel.User>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<dataModel.User>(u, HttpStatus.OK);
    }

    /*
    @RequestMapping(method = GET)
    public dataModel.User getUser() {
        dataModel.User u =
                new dataModel.User(22, "Banhidi Zoltan", "banhidi", "noemim");
        return u;
    }
    */

}
