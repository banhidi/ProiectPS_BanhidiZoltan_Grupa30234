package storageModel;

import dataModel.User;

/**
 * Created by banhidi on 5/22/2017.
 */
public interface UserStorage {

    User getUser(String username, String password);

}
