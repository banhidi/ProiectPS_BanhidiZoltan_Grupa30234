package presenter;

import dataModel.LightweightUserData;
import dataModel.User;
import dataModel.UserData;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 * Created by banhidi on 5/28/2017.
 */
public interface IRegularView {

    void setUser(String name, String username, int id);
    void showErrorMessage(String message);
    void setUsersList(ObservableList<User> usersList);
    User getSelectedUser();
    void clearConversation();
    void appendConversation(String text);
    void setUserListChangeListener(ListChangeListener listener);
    String getMessage();
    void clearMessage();
    void setOnCloseEvent(EventHandler<WindowEvent> e);
    void showLoginView();

    ObservableList<LightweightUserData> getSharedFilesList();
    void setSharedFilesList(ObservableList<LightweightUserData> fileList);
    LightweightUserData getSelectedSharedFile();
    void showImageView(UserData userData);
    void showFileView(UserData userData);
    void setErrorLabel(String text);

}
