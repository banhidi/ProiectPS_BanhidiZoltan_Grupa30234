package storageModel;

/**
 * Created by banhidi on 5/22/2017.
 */
public class StorageBuilder {

    public static UserStorage getUserStorage() {
        return new MySqlUserStorage();
    }

    public static MySqlMessageStorage getMessageStorage() {
        return new MySqlMessageStorage();
    }

    public static UserDataStorage getUserDataStorage() {
        return new MySqlUserDataStorage();
    }

}
