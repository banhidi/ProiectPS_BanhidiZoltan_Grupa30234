package presenter;

import dataModel.UserData;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.apache.tomcat.util.http.fileupload.FileUtils;
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
public class ImagePresenter {

    private final IImageView imageView;
    private UserData userData;

    public ImagePresenter(IImageView imageView) {
        this.imageView = imageView;
        userData = null;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        if (userData.getDescription() != null)
            imageView.setDescription(userData.getDescription());
        if (userData.getData() != null) {
            imageView.setImage(new Image(
                    new ByteArrayInputStream(userData.getData()),
                    300,
                    200,
                    true,
                    false
            ));
        }
    }

    public void chooseFile() {
        File f = imageView.showFileChooser();
        if (f.length() > 8 * Math.pow(10, 6)) {
            imageView.showErrorMessage("File must be <= 8 MB.");
        } else {

            Runnable run = () -> {
                if (userData == null) {
                    imageView.showErrorMessage("Internal system error.");
                } else {
                    try {
                        userData.setData(Files.readAllBytes(f.toPath()));
                        imageView.setImage(new Image(
                                new ByteArrayInputStream(userData.getData()),
                                300,
                                200,
                                true,
                                false
                        ));
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            imageView.showErrorMessage("Cant't read from specified file.");
                        });
                    } catch (Exception e) {

                    }
                }
            };

            new Thread(run).start();
        }
    }

    public void uploadFile() {
        if (userData.getData() != null) {
            userData.setDescription(imageView.getDescription());
            imageView.showProgressBox();

            Runnable run = () -> {
                userData.setChangedData(true);
                userData.setType("image");
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
                        imageView.closeProgressBox();
                        imageView.close();
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        imageView.closeProgressBox();
                        imageView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        imageView.closeProgressBox();
                        imageView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        imageView.closeProgressBox();
                        imageView.showErrorMessage("Internal server error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        } else {
            imageView.showErrorMessage("Please select a file.");
        }
    }

    public void removeFile() {
        if (userData.getId() != -1) {
            imageView.showProgressBox();
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
                        imageView.closeProgressBox();
                        imageView.close();
                    });
                } catch (HttpServerErrorException e) {
                    Platform.runLater(() -> {
                        imageView.closeProgressBox();
                        imageView.showErrorMessage(e.getResponseBodyAsString());
                    });
                } catch (RestClientException e) {
                    Platform.runLater(() -> {
                        imageView.closeProgressBox();
                        imageView.showErrorMessage("Can't connect to the server.");
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        imageView.closeProgressBox();
                        imageView.showErrorMessage("Internal server error.");
                        System.exit(0);
                    });
                }
            };

            new Thread(run).start();
        } else {
            imageView.showErrorMessage("Nothing to remove.");
        }
    }

    public void saveImage() {
        if (userData.getData() != null) {
            File f = imageView.directoryChooser();
            if (f != null) {
                String name = imageView.saveFileDialog();
                if (name != null && !name.isEmpty()) {
                    imageView.showProgressBox();
                    Runnable run = () -> {
                        try {
                            OutputStream out = new FileOutputStream(f.getPath() + "/" + name);
                            out.write(userData.getData());
                            out.close();
                            Platform.runLater(() -> {
                                imageView.closeProgressBox();
                            });
                        } catch (FileNotFoundException e) {
                            Platform.runLater(() -> {
                                imageView.closeProgressBox();
                                imageView.showErrorMessage("Can't open specified file.");
                            });
                        } catch (IOException e) {
                            Platform.runLater(() -> {
                                imageView.closeProgressBox();
                                imageView.showErrorMessage("Error when writeing to file.");
                            });
                        }
                    };

                    new Thread(run).start();
                } else {
                    imageView.showErrorMessage("Invalid name.");
                }
            }
        }
    }

}
