package presenter;

import dataModel.UserData;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * Created by banhidi on 5/31/2017.
 */
public class DataViewPresenter {

    private final IDataView dataView;
    private UserData userData;

    public DataViewPresenter(IDataView dataView) {
        this.dataView = dataView;
        userData = null;
    }

    public void setUserData(UserData u) {
        userData = u;
        if (userData.getId() >= 0)
            updateUserData();
    }

    public void setUserDataText(String text) {
        if (userData == null) {
            dataView.showErrorMessage("Internal error. User's file data is not available.");
            System.exit(0);
        } else
            userData.setData(text.getBytes());
    }

    public void updateUserData() {
        if (userData.getData() != null) {
            dataView.removeRepresentationContainer();
            switch (userData.getType().toLowerCase()) {
                case "image":
                    Image img = new Image(new ByteArrayInputStream(userData.getData()),
                            dataView.getRepresentationWidth(),
                            dataView.getRepresentationHeight(),
                            true,
                            true);
                    dataView.setImage(img);
                    break;
                case "text":
                    String text = new String(userData.getData(), StandardCharsets.UTF_8);
                    dataView.setText(text);
                    break;
                default:
                    Image image = new Image("/pictures/NoPreviewAvailable.png");
                    dataView.setImage(image);
                    break;
            }
            dataView.setDescription(userData.getDescription());
            dataView.setDateTime(userData.getLastModified().toLocalDate());
            dataView.setTypeItem(userData.getType());
        }
    }

    public void readFile() {
        File file = dataView.showFileChooser();
        if (file.length() > 8 * Math.pow(10, 8)) {
            dataView.showErrorMessage("Selected file is too large. Maximum space is 8 MB.");
        } else {
            userData = new UserData(-1, userData.getUserIdA(), userData.getUserIdB());
            userData.setDescription(dataView.getDescription());
            userData.setType(dataView.getTypeItem());
            userData.setLastModified(LocalDateTime.now());
            dataView.showProgressBox("Reading...");

            Runnable run = () -> {
                try {
                    userData.setData(Files.readAllBytes(file.toPath()));

                    Platform.runLater(() -> {
                        dataView.closeProgressBox();
                        updateUserData();
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        dataView.closeProgressBox();
                        dataView.showErrorMessage("Failed to read file.");
                    });
                }
            };

            new Thread(run).start();
        }
    }

    public void changeComboBoxProperty() {
        String value = dataView.getTypeItem();
        dataView.removeRepresentationContainer();
        switch (value) {
            case "image":
                if (userData.getData() != null) {
                    Image img = new Image(new ByteArrayInputStream(userData.getData()),
                            dataView.getRepresentationWidth(),
                            dataView.getRepresentationHeight(),
                            true,
                            true);
                    dataView.setImage(img);
                } else {
                    Image img = new Image("/pictures/NoPreviewAvailable.png",
                            dataView.getRepresentationWidth(),
                            dataView.getRepresentationHeight(),
                            true,
                            true);
                    dataView.setImage(img);
                }
                break;
            case "text":
                if (userData.getData() != null)
                    dataView.setText(new String(userData.getData(), StandardCharsets.UTF_8));
                else
                    dataView.setText("");
                break;
            default:
                Image image = new Image("/pictures/NoPreviewAvailable.png");
                dataView.setImage(image);
                break;
        }
    }

    public void sendFileToServer() {
        if (userData.getData() == null) {
            dataView.showErrorMessage("Please select a file.");
        } else if (userData.getData().length == 0) {
            dataView.showErrorMessage("File is empty.");
        } else{
            userData.setType(dataView.getTypeItem());
            userData.setDescription(dataView.getDescription());
            userData.setLastModified(LocalDateTime.now());
            dataView.showProgressBox("Upoading...");
            userData.setChangedData(true);


            Runnable run = () -> {
                try {
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(userData, header);
                    new RestTemplate().exchange(
                            "http://localhost:8080/api/userData",
                            HttpMethod.PUT,
                            entity,
                            Object.class
                    );
                    Platform.runLater(() -> {
                        dataView.closeProgressBox();
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        dataView.closeProgressBox();
                        dataView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        dataView.closeProgressBox();
                        dataView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        dataView.closeProgressBox();
                        dataView.showErrorMessage("Internal server error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        }
    }

}
