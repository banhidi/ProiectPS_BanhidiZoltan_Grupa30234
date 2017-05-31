package presenter;

import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Created by banhidi on 5/24/2017.
 */
public interface ILoginView {

    String getUsername();
    void clearUsername();

    String getPassword();
    void clearPassword();

    void showErrorMessage(String message);
    void setErrorLabel(String message);

    Stage getStage();

    void setProgressIndicatorVisible(boolean b);
    void showAdminView(String name, String username);
    void showRegularView(String name, String username, int id);

}
