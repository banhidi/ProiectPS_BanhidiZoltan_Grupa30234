package presenter;

import dataModel.User;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;

/**
 * Created by banhidi on 5/24/2017.
 */
public interface IAdminView {

    void setUser(String name, String username);
    void showLoginView();
    void setUsers(ObservableList<User> usersList);
    void showErrorMessage(String message);
    User getSelectedUser();
    void addSelectionChangeListener(ChangeListener listener);

    String getName();
    void setName(String name);

    String getUsername();
    void setUsername(String username);

    String getPassword1();
    void setPassword1(String password);

    String getPassword2();
    void setPassword2(String password);

    String getType();
    void setType(String type);

}
