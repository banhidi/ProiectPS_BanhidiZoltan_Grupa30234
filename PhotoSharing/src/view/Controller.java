package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void loginButtonAction(ActionEvent ae) {
        System.out.println(usernameField.getText() + " " + passwordField.getText());
    }

}
