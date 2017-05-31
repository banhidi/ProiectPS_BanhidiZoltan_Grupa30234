package dataModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by banhidi on 5/29/2017.
 */
public class Conversation {

    private final User userA;
    private final User userB;
    private final List<Message> messages;

    public Conversation(@JsonProperty("userA") User userA,
                        @JsonProperty("userB") User userB,
                        @JsonProperty("messages") List<Message> messages) {
        this.userA = userA;
        this.userB = userB;
        this.messages = messages;
    }

    @JsonProperty("userA")
    public User getUserA() {
        return userA;
    }

    @JsonProperty("userB")
    public User getUserB() {
        return userB;
    }

    @JsonProperty("messages")
    public List<Message> getMessages() {
        return messages;
    }

    public String formatConversation() {
        int userIdA = userA.getId();
        String userNameA = userA.getName();

        int userIdB = userB.getId();
        String userNameB = userB.getName();

        StringBuilder sb = new StringBuilder();
        for(Message m : messages) {
            if (m.getSrcUserId() == userIdA && m.getDstUserId() == userIdB)
                sb.append(userNameA + " -> " + m.getMessage() + "\n");
            else if (m.getSrcUserId() == userIdB && m.getDstUserId() == userIdA)
                sb.append(userNameB + " -> " + m.getMessage() + "\n");
            else
                throw new RuntimeException("Invalid messages.");
        }
        return sb.toString();
    }

}
