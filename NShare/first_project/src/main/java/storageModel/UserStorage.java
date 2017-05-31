package storageModel;

import dataModel.User;

import java.util.List;

/**
 * Created by banhidi on 5/22/2017.
 */
public interface UserStorage {

    User getUser(int id) throws Exception;
    User getUser(String username) throws Exception;
    User getUser(String username, String password) throws Exception;
    List<User> getAllUsers(String type) throws Exception;
    List<User> getAllUsers() throws Exception;
    void insertUser(User u) throws Exception;
    void deleteUser(int id) throws Exception;
    void modifyUser(User u) throws Exception;

}
