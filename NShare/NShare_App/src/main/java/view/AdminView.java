package view;

import dataModel.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import presenter.AdminPresenter;
import presenter.IAdminView;

import java.io.IOException;

/**
 * Created by banhidi on 5/24/2017.
 */
public class AdminView implements IAdminView {

    private AdminPresenter presenter;

    @FXML private Label userLabel;
    @FXML private TableView usersTable;
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField1;
    @FXML private PasswordField passwordField2;
    @FXML private RadioButton adminRadioButton;
    @FXML private RadioButton regularRadioButton;

    @FXML
    public void initialize() {
        presenter = new AdminPresenter(this);
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Override
    public void setUser(String name, String username) {
        userLabel.setText(name + " (" + username + ") ");
    }

    @Override
    public void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginForm.fxml"));
            Parent p = loader.load();

            Scene scene = new Scene(p);
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch(IOException e) {
            showErrorMessage("Internal error. Can't find login form.");
            Platform.exit();
        }
    }

    @Override
    public void setUsers(ObservableList<User> usersList) {
        usersTable.setItems(usersList);
    }

    @Override
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public User getSelectedUser() {
        return (User)usersTable.getSelectionModel().getSelectedItem();
    }

    @Override
    public void addSelectionChangeListener(ChangeListener listener) {
        usersTable.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public void setName(String name) {
        nameField.setText(name);
    }

    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public void setUsername(String username) {
        usernameField.setText(username);
    }

    @Override
    public String getPassword1() {
        return passwordField1.getText();
    }

    @Override
    public void setPassword1(String password) {
        passwordField1.setText(password);
    }

    @Override
    public String getPassword2() {
        return passwordField2.getText();
    }

    @Override
    public void setPassword2(String password) {
        passwordField2.setText(password);
    }

    @Override
    public String getType() {
        if (adminRadioButton.isSelected())
            return "admin";
        else
            return "regular";
    }

    @Override
    public void setType(String type) {
        switch(type) {
            case "admin":
                adminRadioButton.setSelected(true);
                break;
            default:
                regularRadioButton.setSelected(true);
                break;
        }
    }

    @FXML
    private void addButtonAction(ActionEvent ae) {
        presenter.addNewUser();
    }

    @FXML
    private void modifyButtonAction(ActionEvent ae) {
        presenter.modifyUser();
    }

    @FXML
    private void removeButtonAction(ActionEvent ae) {
        presenter.removeUser();
    }

    @FXML
    private void logoutButtonAction(ActionEvent ae) {
        presenter.logout();
    }

}
