package storageModel;

import dataModel.LightweightUserData;
import dataModel.UserData;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by banhidi on 5/30/2017.
 */
class MySqlUserDataStorage implements UserDataStorage {

    private final String connUrl = "jdbc:mysql://localhost:3306/PhotoSharing";
    private final String connUser = "zoli";
    private final String connPassword = "noemi";

    @Override
    public List<LightweightUserData> getLightweightUserDataList(int userIdA, int userIdB) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "select Id, UserIdA, UserIdB, Description, Type from UserData where (UserIdA = ? and UserIdB = ?) or (UserIdA = ? and UserIdB = ?);"
            );
            pst.setInt(1, userIdA);
            pst.setInt(2, userIdB);
            pst.setInt(3, userIdB);
            pst.setInt(4, userIdA);
            rs = pst.executeQuery();
            List<LightweightUserData> list = new LinkedList<>();
            while (rs.next()) {
                LightweightUserData userData =
                        new LightweightUserData(rs.getInt("Id"), rs.getInt("UserIdA"), rs.getInt("UserIdB"));
                userData.setDescription(rs.getString("Description"));
                userData.setType(rs.getString("Type"));
                list.add(userData);
            }
            return list;
        } catch(SQLException e) {
            throw new RuntimeException("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (rs != null)
                    rs.close();
            } catch(SQLException e) {}
        }
    }

    @Override
    public void addUserData(UserData u) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "insert into UserData(UserIdA, UserIdB, DataBlob, Description, Type, LastModified) values " +
                            "(?, ?, ?, ?, ?, ?);");
            pst.setInt(1, u.getUserIdA());
            pst.setInt(2, u.getUserIdB());
            pst.setBytes(3, u.getData());
            pst.setString(4, u.getDescription());
            pst.setString(5, u.getType());
            pst.setString(6, u.getLastModified().toString());
            pst.execute();
        } catch(SQLException e) {
            throw new RuntimeException("Error when connecting to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch(SQLException e) {}
        }
    }

    @Override
    public void deleteUserData(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "delete from UserData where Id = ?;"
            );
            pst.setInt(1, id);
            pst.execute();
        } catch(SQLException e) {
            throw new RuntimeException("Error when connecting to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch(SQLException e) {}
        }
    }

    @Override
    public void modifyUserData(UserData u) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "update UserData set DataBlob = ?, Description = ?, Type = ?, LastModified = ? where Id = ?;"
            );
            pst.setBytes(1, u.getData());
            pst.setString(2, u.getDescription());
            pst.setString(3, u.getType());
            pst.setString(4, u.getLastModified().toString());
            pst.setInt(5, u.getId());
            pst.execute();
        } catch(SQLException e) {
            throw new RuntimeException("Error when connecting to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch(SQLException e) {}
        }
    }

    @Override
    public UserData getUserData(int id) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "select * from UserData where Id = ?;"
            );
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                UserData u = new UserData(
                        rs.getInt("Id"),
                        rs.getInt("UserIdA"),
                        rs.getInt("UserIdB")
                );
                u.setLastModified(rs.getTimestamp("LastModified").toLocalDateTime());
                u.setData(rs.getBytes("DataBlob"));
                u.setType(rs.getString("Type"));
                u.setChangedData(true);
                u.setDescription(rs.getString("Description"));
                return u;
            }
            return null;
        } catch(SQLException e) {
            throw new RuntimeException("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (rs != null)
                    rs.close();
            } catch(SQLException e) {}
        }
    }

}
