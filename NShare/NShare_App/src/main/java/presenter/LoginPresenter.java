package presenter;

import dataModel.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by banhidi on 5/24/2017.
 */
public class LoginPresenter {

    private final ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    public void login() {
        Runnable loginTask = () -> {
            RestTemplate temp = new RestTemplate();
            try {
                ResponseEntity<?> response = temp.getForEntity(
                        "http://localhost:8080/api/users?username={username}&password={password}",
                        User.class,
                        loginView.getUsername(),
                        loginView.getPassword());
                Platform.runLater(() -> {
                    try {
                        User adminUser = (User) response.getBody();

                        FXMLLoader loader = new FXMLLoader(loginView.getClass().getResource("AdminForm.fxml"));
                        Parent p = loader.load();
                        Scene scene = new Scene(p, 300, 300);

                        IAdminView adminView = loader.getController();
                        adminView.setUser(adminUser.getName(), adminUser.getUsername());

                        Stage stage = loginView.getStage();
                        stage.setScene(scene);
                        stage.show();
                    } catch(Exception ex) {
                        loginView.showErrorMessage("Internal system error.");
                    }
                    System.out.println(response.getBody());
                });
            } catch (ResourceAccessException ex) {
                Platform.runLater(() ->
                        loginView.setErrorLabel("Can't connect to the server"));
            } catch (HttpClientErrorException ex) {
                Platform.runLater(() ->
                        loginView.setErrorLabel("Invalid username or password"));
            } catch (RestClientException ex) {
                Platform.runLater(() -> {
                    loginView.showErrorMessage("Internal system error.");
                    Platform.exit();
                });
            }
        };
        Thread t = new Thread(loginTask);
        t.start();
    }

}
