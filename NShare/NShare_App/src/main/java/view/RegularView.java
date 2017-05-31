package view;

import dataModel.LightweightUserData;
import dataModel.User;
import dataModel.UserData;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import presenter.IDataView;
import presenter.IImageView;
import presenter.IRegularView;
import presenter.RegularPresenter;

import java.io.IOException;

/**
 * Created by banhidi on 5/28/2017.
 */
public class RegularView implements IRegularView {

    private RegularPresenter presenter;
    @FXML
    private Label userLabel;
    @FXML
    private ListView<User> usersList;
    @FXML
    private TextArea conversationTextArea;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private TableView sharedFilesTable;

    public void initialize() {
        presenter = new RegularPresenter(this);
        sharedFilesTable.setOnMouseClicked((e) -> {
            if (e.getClickCount() == 2)
                presenter.fileDoubleClicked();
        });
    }

    @Override
    public void setUser(String name, String username, int id) {
        userLabel.setText(name + " (" + username + ") ");
        presenter.setUserId(id);
    }

    @Override
    public void showErrorMessage(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(message);
        a.showAndWait();
    }

    @Override
    public void setUsersList(ObservableList<User> usersList) {
        this.usersList.setItems(usersList);
    }

    @Override
    public User getSelectedUser() {
        return usersList.getSelectionModel().getSelectedItem();
    }

    @Override
    public void clearConversation() {
        conversationTextArea.clear();
    }

    @Override
    public void appendConversation(String text) {
        conversationTextArea.appendText(text);
        conversationTextArea.setScrollTop(Double.MAX_VALUE);
    }

    @Override
    public void setUserListChangeListener(ListChangeListener listener) {
        usersList.getSelectionModel().getSelectedItems().addListener(listener);
    }

    @Override
    public String getMessage() {
        return messageTextArea.getText();
    }

    @Override
    public void clearMessage() {
        messageTextArea.clear();
    }

    @Override
    public void setOnCloseEvent(EventHandler e) {
        userLabel.getScene().getWindow().setOnCloseRequest(e);
    }

    @Override
    public void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
            Parent p = loader.load();

            ((Stage) userLabel.getScene().getWindow()).setScene(new Scene(p));
        } catch (Exception e) {
            showErrorMessage("Internal system error occured.");
            Platform.exit();
        }
    }

    @Override
    public ObservableList<LightweightUserData> getSharedFilesList() {
        return sharedFilesTable.getItems();
    }

    @Override
    public void setSharedFilesList(ObservableList<LightweightUserData> fileList) {
        sharedFilesTable.setItems(fileList);
    }

    @Override
    public LightweightUserData getSelectedSharedFile() {
        return (LightweightUserData) sharedFilesTable.getSelectionModel().getSelectedItem();
    }

    @Override
    public void showImageView(UserData userData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageForm.fxml"));
            Parent p = loader.load();

            IImageView imageView = (IImageView) loader.getController();
            imageView.setUserData(userData);

            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.show();
        } catch(IOException e) {
            showErrorMessage("Internal system error: can't load data form.");
            System.exit(0);
        }
    }

    @FXML
    private void sendButtonAction(ActionEvent ae) {
        presenter.sendMessage();
    }

    @FXML
    private void logoutButtonAction(ActionEvent ae) {
        presenter.logout();
    }

    @FXML
    private void addNewFileButtonAction(ActionEvent ae) {
        presenter.addNewFile();
    }

}
