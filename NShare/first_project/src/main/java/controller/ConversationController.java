package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dataModel.Conversation;
import dataModel.Message;
import dataModel.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import storageModel.StorageBuilder;
import storageModel.UserStorage;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by banhidi on 5/28/2017.
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getConversation(@PathParam(value = "userIdA") int userIdA,
                                          @PathParam(value = "userIdB") int userIdB) {
        try {
            UserStorage userStorage = StorageBuilder.getUserStorage();
            User userA = userStorage.getUser(userIdA);
            User userB = userStorage.getUser(userIdB);
            if (userA != null && userB != null) {
                List<Message> messages = StorageBuilder.getMessageStorage().getAllMessages(userIdA, userIdB);
                Conversation conversation = new Conversation(userA, userB, messages);
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String r = mapper.writeValueAsString(conversation);
                return new ResponseEntity<String>(r, HttpStatus.OK);
            } else {
                return new ResponseEntity("Invalid user(s) specified.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity putNewConversation(@RequestBody Message m) {
        try {
            UserStorage userStorage = StorageBuilder.getUserStorage();
            User userA = userStorage.getUser(m.getSrcUserId());
            User userB = userStorage.getUser(m.getDstUserId());
            if (userA == null || userB == null) {
                return new ResponseEntity("Invalid user(s) specified.", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                StorageBuilder.getMessageStorage().addMessage(m);
                return new ResponseEntity(null, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
