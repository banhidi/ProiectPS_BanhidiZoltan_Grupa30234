package storageModel;

import dataModel.User;

import java.sql.*;

/**
 * Created by banhidi on 5/21/2017.
 */
class MySqlUserStorage implements UserStorage {

    public User getUser(String username, String password) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PhotoSharing",
                    "zoli",
                    "noemi");
            PreparedStatement pst = conn.prepareStatement(
                    "select * from User where Username = ? and Password = ?;");
            pst.setString(1, username);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Username"),
                        rs.getString("Password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return null;
    }

}
