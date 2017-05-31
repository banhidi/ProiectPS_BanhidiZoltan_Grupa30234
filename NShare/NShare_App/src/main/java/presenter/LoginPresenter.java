package presenter;

import dataModel.User;
import javafx.application.Platform;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
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
            } catch (HttpServerErrorException ex) {
                Platform.runLater(() -> {
                    loginView.setErrorLabel(ex.getResponseBodyAsString());
                    loginView.setProgressIndicatorVisible(false);
                });
            } catch (RestClientException ex) {
                Platform.runLater(() -> {
                    loginView.setProgressIndicatorVisible(false);
                    loginView.showErrorMessage("Internal system error occured when communicating with the server.");
                });
            } catch(NoSuchAlgorithmException ex) {
                loginView.setProgressIndicatorVisible(false);
                loginView.showErrorMessage("Internal system error. Can't found hash function.");
                System.exit(0);
            } catch(Exception e) {
                loginView.showErrorMessage("Internal system error occured.");
                System.exit(0);
            }
        };

        Thread t = new Thread(loginTask);
        t.start();
    }

}
