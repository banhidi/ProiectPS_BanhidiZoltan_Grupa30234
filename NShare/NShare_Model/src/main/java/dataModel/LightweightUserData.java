package dataModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by banhidi on 5/31/2017.
 */
public class LightweightUserData {

    @JsonProperty("id")
    protected final int id;
    @JsonProperty("userIdA")
    protected final int userIdA;
    @JsonProperty("userIdB")
    protected final int userIdB;
    @JsonProperty("description")
    private StringProperty description;
    @JsonProperty("type")
    private StringProperty type;

    public LightweightUserData(@JsonProperty("id") int id,
                               @JsonProperty("userIdA") int userIdA,
                               @JsonProperty("userIdB") int userIdB) {
        this.id = id;
        this.userIdA = userIdA;
        this.userIdB = userIdB;
        description = new SimpleStringProperty();
        type = new SimpleStringProperty();
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("userIdA")
    public int getUserIdA() {
        return userIdA;
    }

    @JsonProperty("userIdB")
    public int getUserIdB() {
        return userIdB;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description.get();
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description.set(description);
    }

    @JsonProperty("type")
    public String getType() {
        return type.get();
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type.set(type);
    }

    public boolean equals(Object o) {
        if (o instanceof LightweightUserData) {
            return ((LightweightUserData) o).getId() == id;
        } else
            return false;
    }

}
