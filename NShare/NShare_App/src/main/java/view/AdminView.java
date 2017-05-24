package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import presenter.IAdminView;

/**
 * Created by banhidi on 5/24/2017.
 */
public class AdminView implements IAdminView {

    @FXML private Label userLabel;

    @Override
    public void setUser(String name, String username) {
        userLabel.setText(name + " (" + username + ") ");
    }

}
