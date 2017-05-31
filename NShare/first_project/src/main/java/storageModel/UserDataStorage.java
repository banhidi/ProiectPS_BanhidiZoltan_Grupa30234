package storageModel;

import dataModel.LightweightUserData;
import dataModel.UserData;

import java.util.List;

/**
 * Created by banhidi on 5/30/2017.
 */
public interface UserDataStorage {

    List<LightweightUserData> getLightweightUserDataList(int userIdA, int userIdB);
    void addUserData(UserData u);
    void deleteUserData(int id);
    void modifyUserData(UserData u);
    UserData getUserData(int id);

}
