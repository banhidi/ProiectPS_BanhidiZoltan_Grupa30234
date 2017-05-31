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

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * Created by banhidi on 5/31/2017.
 */
public class FilePresenter {

    private final IFileView fileView;
    private UserData userData;

    public FilePresenter(IFileView fileView) {
        this.fileView = fileView;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        if (userData.getDescription() != null)
            fileView.setDescription(userData.getDescription());
        if (userData.getLastModified() != null)
            fileView.setDatePicker(userData.getLastModified().toLocalDate());
    }

    public void chooseFile() {
        File f = fileView.showFileChooser();
        if (f.length() > 8 * Math.pow(10, 6)) {
            fileView.showErrorMessage("File must be <= 8 MB.");
        } else {

            Runnable run = () -> {
                if (userData == null) {
                    fileView.showErrorMessage("Internal system error.");
                } else {
                    try {
                        userData.setData(Files.readAllBytes(f.toPath()));
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            fileView.showErrorMessage("Cant't read from specified file.");
                        });
                    }
                }
            };

            new Thread(run).start();
        }
    }

    public void saveFile() {
        if (userData.getData() != null) {
            File f = fileView.directoryChooser();
            if (f != null) {
                String name = fileView.saveFileDialog();
                if (name != null && !name.isEmpty()) {
                    fileView.showProgressBox();
                    Runnable run = () -> {
                        try {
                            OutputStream out = new FileOutputStream(f.getPath() + "/" + name);
                            out.write(userData.getData());
                            out.close();
                            Platform.runLater(() -> {
                                fileView.closeProgressBox();
                            });
                        } catch (FileNotFoundException e) {
                            Platform.runLater(() -> {
                                fileView.closeProgressBox();
                                fileView.showErrorMessage("Can't open specified file.");
                            });
                        } catch (IOException e) {
                            Platform.runLater(() -> {
                                fileView.closeProgressBox();
                                fileView.showErrorMessage("Error when writeing to file.");
                            });
                        }
                    };

                    new Thread(run).start();
                } else {
                    fileView.showErrorMessage("Invalid name.");
                }
            }
        } else {
            fileView.showErrorMessage("Nothing to save.");
        }
    }

    public void uploadFile() {
        if (userData.getData() != null) {
            userData.setDescription(fileView.getDescription());
            fileView.showProgressBox();

            Runnable run = () -> {
                userData.setChangedData(true);
                userData.setType("other");
                userData.setLastModified(LocalDateTime.now());

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
                        fileView.closeProgressBox();
                        fileView.close();
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.showErrorMessage("Internal server error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        } else {
            fileView.showErrorMessage("Please select a file.");
        }
    }

    public void removeFile() {
        if (userData.getId() != -1) {
            fileView.showProgressBox();
            Runnable run = () -> {
                try {
                    HttpHeaders header = new HttpHeaders();
                    header.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity entity = new HttpEntity(null, header);
                    new RestTemplate().exchange(
                            "http://localhost:8080/api/userData?id=" + userData.getId(),
                            HttpMethod.DELETE,
                            entity,
                            Object.class
                    );
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.close();
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        fileView.closeProgressBox();
                        fileView.showErrorMessage("Internal server error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        } else {
            fileView.showErrorMessage("Nothing to remove.");
        }
    }

}
