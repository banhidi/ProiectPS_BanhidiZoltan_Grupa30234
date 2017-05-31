package presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dataModel.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by banhidi on 5/28/2017.
 */
public class RegularPresenter {

    private int userId;
    private final IRegularView regularView;
    private final Thread reloadThread;
    private String lastConversation;

    public RegularPresenter(IRegularView regularView) {
        this.regularView = regularView;
        lastConversation = "";
        reloadRegularList();
        regularView.setUserListChangeListener((c) -> {
            reloadConversation();
        });

        Runnable run = () -> {
            boolean b = true;
            while (b) {
                try {
                reloadConversation();
                reloadSharedFiles();
                //System.out.println("Reload!");

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    b = false;
                }
            }
        };

        reloadThread = new Thread(run);
        reloadThread.start();
    }

    public void setUserId(int id) {
        userId = id;
    }

    public void addNewFile() {
        User u = regularView.getSelectedUser();
        if (u == null) {
            regularView.showErrorMessage("Please select a user.");
        } else
            regularView.showImageView(new UserData(-1, userId, u.getId()));
    }

    public void fileDoubleClicked() {
        LightweightUserData selectedUserData = regularView.getSelectedSharedFile();
        if (selectedUserData == null)
            regularView.showErrorMessage("Please select a file.");
        else {
            Runnable run = () -> {
                try {
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(null, header);
                    ResponseEntity<UserData> response = new RestTemplate().exchange(
                            "http://localhost:8080/api/userData?id=" + selectedUserData.getId(),
                            HttpMethod.GET,
                            entity,
                            UserData.class
                    );
                    UserData userData = response.getBody();
                    Platform.runLater(() -> {
                        regularView.showImageView(userData);
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Internal system error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        }
    }

    private void reloadRegularList() {
        Runnable run = () -> {
            try {
                HttpHeaders header = new HttpHeaders();
                header.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity entity = new HttpEntity(null, header);
                ResponseEntity response = new RestTemplate().exchange(
                        "http://localhost:8080/api/users/regular",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<User>>() {
                        }
                );
                ObservableList<User> usersList = FXCollections.observableArrayList((List<User>) response.getBody());
                Platform.runLater(() -> {
                    regularView.setUsersList(usersList);
                });
            } catch (HttpServerErrorException e) {
                Platform.runLater(() -> {
                    regularView.showErrorMessage(e.getResponseBodyAsString());
                });
            } catch (RestClientException e) {
                Platform.runLater(() -> {
                    regularView.showErrorMessage("Can't connect to the server.");
                });
            }
        };

        new Thread(run).start();
    }

    private void reloadConversation() {
        User selectedUser = regularView.getSelectedUser();
        if (selectedUser != null) {
            Runnable run = () -> {
                try {
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(null, header);
                    ResponseEntity<String> response = new RestTemplate().exchange(
                            "http://localhost:8080/api/conversations?userIdA=" + userId + "&userIdB=" + selectedUser.getId(),
                            HttpMethod.GET,
                            entity,
                            String.class
                    );
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    Conversation conversation = mapper.readValue(response.getBody(),
                            Conversation.class);
                    String text = conversation.formatConversation();
                    if (!text.equals(lastConversation)) {
                        Platform.runLater(() -> {
                            regularView.clearConversation();
                            regularView.appendConversation(text);
                        });
                        lastConversation = text;
                    }

                    regularView.setOnCloseEvent((e) -> {
                        System.exit(0);
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Internal system error.");
                        Platform.exit();
                    });
                }
            };

            new Thread(run).start();
        }
    }

    public void reloadSharedFiles() {
        User selectedUser = regularView.getSelectedUser();
        if (selectedUser != null) {
            Runnable run = () -> {
                try {
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(null, header);
                    ResponseEntity<List<LightweightUserData>> response = new RestTemplate().exchange(
                            "http://localhost:8080/api/userData/lightweight?userIdA=" + userId + "&userIdB=" + selectedUser.getId(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<LightweightUserData>>() {}
                    );
                    List<LightweightUserData> sharedFilesList = response.getBody();
                    if (!regularView.getSharedFilesList().equals(sharedFilesList))
                        Platform.runLater(() -> {
                            regularView.setSharedFilesList(FXCollections.observableArrayList(sharedFilesList));
                        });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Internal system error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        }
    }

    public void sendMessage() {
        String stringMessage = regularView.getMessage();
        User selectedUser = regularView.getSelectedUser();
        if (selectedUser == null)
            regularView.showErrorMessage("Please select a user.");
        else if (stringMessage.isEmpty())
            regularView.showErrorMessage("Please introduce a message.");
        else {
            Runnable run = () -> {
                Message message = new Message(
                        userId,
                        selectedUser.getId(),
                        LocalDateTime.now(),
                        stringMessage
                );

                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(message, headers);
                    new RestTemplate().exchange(
                            "http://localhost:8080/api/conversations",
                            HttpMethod.PUT,
                            entity,
                            Message.class
                    );
                    Platform.runLater(() -> {
                        regularView.clearMessage();
                    });
                    reloadConversation();
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        regularView.showErrorMessage("Internal system error.");
                        Platform.exit();
                    });
                }
            };

            new Thread(run).start();
        }
    }

    public void logout() {
        reloadThread.interrupt();
        regularView.showLoginView();
    }

}
