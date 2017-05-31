package dataModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

/**
 * Created by banhidi on 5/30/2017.
 */
public class UserData extends LightweightUserData {

    @JsonProperty("data")
    private byte[] data;
    @JsonProperty("lastModified")
    private LocalDateTime lastModified;
    @JsonProperty("changedData")
    private boolean changedData;

    public UserData(@JsonProperty("id") int id,
                    @JsonProperty("userIdA") int userIdA,
                    @JsonProperty("userIdB") int userIdB) {
        super(id, userIdA, userIdB);
        changedData = false;
    }

    @JsonProperty("data")
    public byte[] getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(byte[] data) {
        this.data = data;
    }

    @JsonProperty("lastModified")
    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @JsonProperty("lastModified")
    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @JsonProperty("changedData")
    public boolean isChangedData() {
        return changedData;
    }

    @JsonProperty("changedData")
    public void setChangedData(boolean changedData) {
        this.changedData = changedData;
    }

}
