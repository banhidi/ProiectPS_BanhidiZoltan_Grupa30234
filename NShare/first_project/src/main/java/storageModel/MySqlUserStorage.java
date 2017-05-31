package storageModel;

import dataModel.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by banhidi on 5/21/2017.
 */
class MySqlUserStorage implements UserStorage {

    private final String connUrl = "jdbc:mysql://localhost:3306/PhotoSharing";
    private final String connUser = "zoli";
    private final String connPassword = "noemi";

    @Override
    public User getUser(int id) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "select * from User where Id = ?;"
            );
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next())
                return new User(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Type")
                );
            else
                return null;
        } catch (SQLException e) {
            throw new Exception("Can't connect to the database.");
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
    public User getUser(String username) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "select * from User where Username = ?;"
            );
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next())
                return new User(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Type")
                );
            else
                return null;
        } catch (SQLException e) {
            throw new Exception("Can't connect to the database.");
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

    public User getUser(String username, String password) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
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
                        rs.getString("Password"),
                        rs.getString("Type"));
            }
        } catch (Exception e) {
            throw new Exception("Can't connect to the database.");
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

    @Override
    public List<User> getAllUsers(String type) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "select * from User where type = ?;"
            );
            pst.setString(1, type);
            rs = pst.executeQuery();
            List<User> usersList = new LinkedList<User>();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("Id"));
                u.setName(rs.getString("Name"));
                u.setUsername(rs.getString("Username"));
                u.setPassword(rs.getString("Password"));
                u.setType(rs.getString("Type"));
                usersList.add(u);
            }
            return usersList;
        } catch (SQLException e) {
            throw new Exception("Can't connect to the database.");
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
    public List<User> getAllUsers() throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement("select * from User;");
            rs = pst.executeQuery();
            List<User> usersList = new LinkedList<User>();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("Id"));
                u.setName(rs.getString("Name"));
                u.setUsername(rs.getString("Username"));
                u.setPassword(rs.getString("Password"));
                u.setType(rs.getString("Type"));
                usersList.add(u);
            }
            return usersList;
        } catch (SQLException e) {
            throw new Exception("Can't connect to the database.");
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

    public void insertUser(User u) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "insert into User(Name, Username, Password, Type) values " +
                            "(?, ?, ?, ?);"
            );
            pst.setString(1, u.getName());
            pst.setString(2, u.getUsername());
            pst.setString(3, u.getPassword());
            pst.setString(4, u.getType());
            pst.execute();
        } catch (SQLException ex) {
            throw new Exception("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public void deleteUser(int id) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "delete from User where Id = ?;"
            );
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new Exception("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public void modifyUser(User u) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            PreparedStatement pst = conn.prepareStatement(
                    "update User set Name = ?, Username = ?, Password = ?, Type = ? where Id = ?;"
            );
            pst.setString(1, u.getName());
            pst.setString(2, u.getUsername());
            pst.setString(3, u.getPassword());
            pst.setString(4, u.getType());
            pst.setInt(5, u.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new Exception("Can't connect to the database.");
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
            }
        }
    }

}
