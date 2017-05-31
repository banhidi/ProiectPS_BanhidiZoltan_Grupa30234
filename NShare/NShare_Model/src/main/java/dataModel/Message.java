package dataModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Created by banhidi on 5/28/2017.
 */
public class Message {

    private final int srcUserId;
    private final int dstUserId;
    private final LocalDateTime dateTime;
    private final String message;

    public Message(@JsonProperty("srcUserId") int srcUserId,
                   @JsonProperty("dstUserId") int dstUserId,
                   @JsonProperty("dateTime") LocalDateTime dateTime,
                   @JsonProperty("message") String message) {
        this.srcUserId = srcUserId;
        this.dstUserId = dstUserId;
        this.dateTime = dateTime;
        this.message = message;
    }

    public int getSrcUserId() {
        return srcUserId;
    }

    public int getDstUserId() {
        return dstUserId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message m = (Message) obj;
            return (m.getSrcUserId() == srcUserId && m.getDstUserId() == dstUserId &&
                    m.getDateTime().equals(dateTime) && m.getMessage().equals(message));
        } else
            return false;
    }

}
