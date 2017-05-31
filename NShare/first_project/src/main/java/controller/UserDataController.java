package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dataModel.LightweightUserData;
import dataModel.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import storageModel.StorageBuilder;

import java.util.List;

/**
 * Created by banhidi on 5/30/2017.
 */
@RestController
@RequestMapping("/api/userData")
public class UserDataController {

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity putUserData(@RequestBody UserData userData) {
        try {
            if (userData.getId() < 0) {
                //ObjectMapper mapper = new ObjectMapper();
                //UserData userData = mapper.readValue(jsonUserData, UserData.class);
                StorageBuilder.getUserDataStorage().addUserData(userData);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                StorageBuilder.getUserDataStorage().modifyUserData(userData);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path="/lightweight")
    public ResponseEntity getAllUserData(@RequestParam(value="userIdA") int userIdA,
                                         @RequestParam(value="userIdB") int userIdB) {
        try {
            List<LightweightUserData> list = StorageBuilder.getUserDataStorage().getLightweightUserDataList(userIdA, userIdB);
            return new ResponseEntity(list, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUserData(@RequestParam(value="id") int id) {
        try {
            UserData u = StorageBuilder.getUserDataStorage().getUserData(id);
            return new ResponseEntity(u, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteUserData(@RequestParam(value="id") int id) {
        try {
            StorageBuilder.getUserDataStorage().deleteUserData(id);
            return new ResponseEntity(null, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
