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
import security.HashSecurity;

import java.security.NoSuchAlgorithmException;

/**
 * Created by banhidi on 5/24/2017.
 */
public class LoginPresenter {

    private final ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    public void login() {
        loginView.setProgressIndicatorVisible(true);
        loginView.setErrorLabel("");
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        Runnable loginTask = () -> {
            RestTemplate temp = new RestTemplate();
            try {
                HashSecurity hashSecurity = new HashSecurity();
                ResponseEntity<?> response = temp.getForEntity(
                        "http://localhost:8080/api/users/authentify?username={username}&password={password}",
                        User.class,
                        username,
                        hashSecurity.applyHashFunction(password));

                User user = (User) response.getBody();
                switch(user.getType()) {
                    case "admin":
                        Platform.runLater(() -> {
                            loginView.showAdminView(user.getName(), user.getUsername());
                            loginView.setProgressIndicatorVisible(false);
                        });
                        break;
                    case "regular":
                        Platform.runLater(() -> {
                            loginView.showRegularView(user.getName(), user.getUsername(), user.getId());
                            loginView.setProgressIndicatorVisible(false);
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            loginView.setProgressIndicatorVisible(false);
                            loginView.showErrorMessage("User has invalid type.");
                        });
                }

            } catch (ResourceAccessException ex) {
                Platform.runLater(() -> {
                    loginView.setErrorLabel("Can't connect to the server.");
                    loginView.setProgressIndicatorVisible(false);
                });
            } catch (HttpClientErrorException ex) {
                Platform.runLater(() -> {
                    loginView.setErrorLabel("Invalid username or password");
                    loginView.setProgressIndicatorVisible(false);
                });
            } catch (RestClientException ex) {
                Platform.runLater(() -> {
                    loginView.setProgressIndicatorVisible(false);
                    loginView.showErrorMessage("Internal system error occured when communicating eith the server.");
                    Platform.exit();
                });
            } catch(NoSuchAlgorithmException ex) {
                loginView.setProgressIndicatorVisible(false);
                loginView.showErrorMessage("Internal system error. Can't found hash function.");
                Platform.exit();
            }
        };
        Thread t = new Thread(loginTask);
        t.start();
    }

}
