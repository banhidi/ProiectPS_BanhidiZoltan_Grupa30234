package dataModel;

import javafx.beans.property.*;

/**
 * Created by banhidi on 5/22/2017.
 */
public class User {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty type;

    public User(int id, String name, String username, String password, String type) {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.type = new SimpleStringProperty();

        this.id.set(id);
        this.name.set(name);
        this.username.set(username);
        this.password.set(password);
        this.type.set(type);
    }

    public User() {
        this(-1, "", "", "", "regular");
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String toString() {
        return name.get() + " (" + username.get() + ")";
    }

}
