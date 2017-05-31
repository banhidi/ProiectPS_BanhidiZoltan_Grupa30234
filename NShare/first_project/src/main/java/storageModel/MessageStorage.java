package storageModel;

import dataModel.Message;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by banhidi on 5/28/2017.
 */
public interface MessageStorage {

    List<Message> getAllMessages(int userIdA, int userIdB);

    List<Message> getMessages(int userIdA, int userIdB, LocalDateTime firstMessage, LocalDateTime lastMessage);

    void addMessage(Message msg);

}
