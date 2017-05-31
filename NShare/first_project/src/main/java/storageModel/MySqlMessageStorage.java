package storageModel;

import dataModel.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by banhidi on 5/28/2017.
 */
public class MySqlMessageStorage implements MessageStorage {

    private final String connUrl = "jdbc:mysql://localhost:3306/PhotoSharing";
    private final String connUser = "zoli";
    private final String connPassword = "noemi";

    @Override
    public List<Message> getAllMessages(int userIdA, int userIdB) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "select * from Message where (FromUserId = ? and ToUserId = ?) or " +
                            "(FromUserId = ? and ToUserId = ?) order by MessageDateTime;"
            );
            pst.setInt(1, userIdA);
            pst.setInt(2, userIdB);
            pst.setInt(3, userIdB);
            pst.setInt(4, userIdA);
            rs = pst.executeQuery();
            List<Message> messages = new LinkedList<Message>();
            while (rs.next()) {
                int sourceUserId = rs.getInt("FromUserId");
                int destinationUserId = rs.getInt("ToUserId");
                LocalDateTime messageDateTime = rs.getTimestamp("MessageDateTime").toLocalDateTime();
                String message = rs.getString("Message");
                messages.add(new Message(sourceUserId, destinationUserId, messageDateTime, message));
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public List<Message> getMessages(int userIdA, int userIdB, LocalDateTime firstMessage, LocalDateTime lastMessage) {
        return null;
    }

    @Override
    public void addMessage(Message msg) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "insert into Message(FromUserId, ToUserId, MessageDateTime, Message) values " +
                            "(?, ?, ?, ?);"
            );
            pst.setInt(1, msg.getSrcUserId());
            pst.setInt(2, msg.getDstUserId());
            String s = msg.getDateTime().toString();
            pst.setString(3, s);
            pst.setString(4, msg.getMessage());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
            }
        }
    }

}
