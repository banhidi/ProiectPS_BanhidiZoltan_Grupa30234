package storageModel;

/**
 * Created by banhidi on 5/22/2017.
 */
public class StorageBuilder {

    public static UserStorage getUserStorage() {
        return new MySqlUserStorage();
    }

}
