package presenter;

import dataModel.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.*;
import security.HashSecurity;
import view.AdminView;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by banhidi on 5/27/2017.
 */
public class AdminPresenter {

    private IAdminView adminView;

    public AdminPresenter(AdminView adminView) {
        this.adminView = adminView;
        adminView.setType("regular");
        reloadUsersTable();
        adminView.addSelectionChangeListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                User u = (User) newValue;
                adminView.setName(u.getName());
                adminView.setUsername(u.getUsername());
                if (u.getType().equals("admin"))
                    adminView.setType("admin");
                else
                    adminView.setType("regular");
            }
        });
    }

    private List<User> getAllUsers() throws RestClientException {
        RestTemplate temp = new RestTemplate();
        ResponseEntity<List<User>> response = temp.exchange("http://localhost:8080/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                }
        );
        List usersList = response.getBody();
        return usersList;
    }

    private void reloadUsersTable() {
        Runnable run = () -> {
            try {
                List<User> usersList = getAllUsers();
                Platform.runLater(() -> {
                    adminView.setUsers(FXCollections.observableArrayList(usersList));
                });
            } catch (ResourceAccessException ex) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("Can't connect to the server.");
                });
            } catch (HttpServerErrorException ex) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("Server has an internal error.");
                });
            } catch (RestClientException ex) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("Can't communicate with the server.");
                });
            }
        };

        new Thread(run).start();
    }

    public void addNewUser() {
        String name = adminView.getName();
        String username = adminView.getUsername();
        String password1 = adminView.getPassword1();
        String password2 = adminView.getPassword2();
        String type = adminView.getType();

        Runnable run = () -> {
            if (!password1.equals(password2)) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("The passwords are different.");
                });
            } else if (name.length() < 3 || username.length() < 3 || password1.length() < 3) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage(
                            "The name, username and password must have length >= 3."
                    );
                });
            } else {
                try {
                    HashSecurity hash = new HashSecurity();
                    User u = new User(-1, name, username, hash.applyHashFunction(password1), type);
                    System.out.println(u.getType());

                    RestTemplate temp = new RestTemplate();
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<User> entity = new HttpEntity(u, header);

                    temp.exchange("http://localhost:8080/api/users",
                            HttpMethod.PUT,
                            entity,
                            Object.class
                    );
                    reloadUsersTable();
                    Platform.runLater(() -> {
                        adminView.setName("");
                        adminView.setUsername("");
                        adminView.setPassword1("");
                        adminView.setPassword2("");
                        adminView.setType("regular");
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch(RestClientException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage("Can't communicate with the server.");
                    });
                } catch (NoSuchAlgorithmException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage("Internal server error. Can't apply hash function on password.");
                        Platform.exit();
                    });
                }
            }
        };

        new Thread(run).start();
    }

    public void removeUser() {
        User u = adminView.getSelectedUser();

        Runnable run = () -> {
            if (u == null) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("Please select an user.");
                });
            } else {
                HttpHeaders header = new HttpHeaders();
                header.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity entity = new HttpEntity(u.getId(), header);
                try {
                    RestTemplate temp = new RestTemplate();
                    temp.exchange(
                            "http://localhost:8080/api/users?id=" + u.getId(),
                            HttpMethod.DELETE,
                            entity,
                            Object.class
                    );
                    reloadUsersTable();
                    Platform.runLater(() -> {
                        adminView.setName("");
                        adminView.setUsername("");
                        adminView.setPassword1("");
                        adminView.setPassword2("");
                        adminView.setType("regular");
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch(RestClientException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage("Can't communicate with the server.");
                    });
                }
            }
        };

        new Thread(run).start();
    }

    public void modifyUser() {
        User u = adminView.getSelectedUser();

        String name = adminView.getName();
        String username = adminView.getUsername();
        String password1 = adminView.getPassword1();
        String password2 = adminView.getPassword2();
        String type = adminView.getType();

        Runnable run = () -> {
            if (u == null) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("Please select an user.");
                });
            } else if (!password1.equals(password2)) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("The passwords are different.");
                });
            } else if (name.length() < 3 || username.length() < 3 || password1.length() < 3) {
                Platform.runLater(() -> {
                    adminView.showErrorMessage("The name, username and password must have length >= 3.");
                });
            } else {
                try {
                    u.setName(name);
                    u.setUsername(username);
                    u.setPassword(new HashSecurity().applyHashFunction(password1));
                    u.setType(type);


                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(u, header);
                    new RestTemplate().exchange(
                            "http://localhost:8080/api/users",
                            HttpMethod.POST,
                            entity,
                            Object.class
                    );
                    reloadUsersTable();
                    Platform.runLater(() -> {
                        adminView.setName("");
                        adminView.setUsername("");
                        adminView.setPassword1("");
                        adminView.setPassword2("");
                        adminView.setType("regular");
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage("Can't communicate with the server.");
                    });
                } catch(NoSuchAlgorithmException e) {
                    Platform.runLater(() -> {
                        adminView.showErrorMessage("Internal server error. Can't apply hash function on password.");
                        Platform.exit();
                    });
                }
            }
        };

        new Thread(run).start();
    }

    public void logout() {
        adminView.showLoginView();
    }

}
