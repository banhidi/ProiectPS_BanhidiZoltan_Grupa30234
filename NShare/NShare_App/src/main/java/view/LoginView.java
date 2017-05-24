package view;

import dataModel.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import presenter.ILoginView;
import presenter.LoginPresenter;

/**
 * Created by banhidi on 5/22/2017.
 */
public class LoginView implements ILoginView {

    private LoginPresenter loginPresenter;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    public LoginView() {
        loginPresenter = new LoginPresenter(this);
    }

    @FXML
    void loginButtonAction(ActionEvent e) {
        loginPresenter.login();
    }

    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public void clearUsername() {
        usernameField.clear();
    }

    @Override
    public String getPassword() {
        return passwordField.getText();
    }

    @Override
    public void clearPassword() {
        passwordField.clear();
    }

    @Override
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.show();
    }

    @Override
    public void setErrorLabel(String message) {
        errorLabel.setText(message);
    }

    @Override
    public Stage getStage() {
        return (Stage) errorLabel.getScene().getWindow();
    }

}
