package view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import presenter.IAdminView;
import presenter.ILoginView;
import presenter.IRegularView;
import presenter.LoginPresenter;

import java.io.IOException;

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
    @FXML
    private ProgressIndicator progressIndicator;

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
        alert.showAndWait();
    }

    @Override
    public void setErrorLabel(String message) {
        errorLabel.setText(message);
    }

    @Override
    public Stage getStage() {
        return (Stage) errorLabel.getScene().getWindow();
    }

    public void setProgressIndicatorVisible(boolean b) {
        progressIndicator.setVisible(b);
    }

    public void showAdminView(String name, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminForm.fxml"));
            Parent p = loader.load();
            Scene scene = new Scene(p);

            IAdminView adminView = loader.getController();
            adminView.setUser(name, username);

            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch(IOException e) {
            showErrorMessage("Internal system error. Can't find admin form.");
            Platform.exit();
        }
    }

    @Override
    public void showRegularView(String name, String username, int id) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginView.class.getResource("/view/RegularForm.fxml"));
            Parent p = loader.load();
            Scene scene = new Scene(p);

            IRegularView regularView = loader.getController();
            regularView.setUser(name, username, id);

            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch(IOException e) {
            //System.out.println(e.getMessage());
            showErrorMessage("Internal system error. Can't find regular form.");
            System.exit(0);
        }
    }

}
