package view;

import dataModel.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import presenter.DataViewPresenter;
import presenter.IDataView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by banhidi on 5/30/2017.
 */
public class DataView implements IDataView {

    private DataViewPresenter presenter;
    private ProgressBox progressBox;
    @FXML private VBox vbox;
    @FXML private DatePicker datePicker;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> typeComboBox;


    public DataView() {
        presenter = new DataViewPresenter(this);
    }

    public void initialize() {
        typeComboBox.valueProperty().addListener((string) -> {
            presenter.changeComboBoxProperty();
        });
    }

    @FXML
    private void modifyButtonAction(ActionEvent ae) {
        presenter.sendFileToServer();
    }

    @Override
    public File showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(vbox.getScene().getWindow());
        return file;
    }

    @Override
    public void showProgressBox(String title) {
        progressBox = new ProgressBox("Progress", vbox.getScene().getWindow());
    }

    @Override
    public void closeProgressBox() {
        if (progressBox != null)
            progressBox.close();
    }

    @Override
    public void showErrorMessage(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }

    @Override
    public void setDateTime(LocalDate d) {
        datePicker.setValue(d);
    }

    @Override
    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    @Override
    public String getDescription() {
        return descriptionField.getText();
    }

    @Override
    public String getTypeItem() {
        return typeComboBox.getValue();
    }

    @Override
    public void setTypeItem(String type) {
        typeComboBox.setValue(type);
    }

    @Override
    public void setImage(Image img) {
        ImageView imageView = new ImageView();
        imageView.setImage(img);
        imageView.prefWidth(200);
        imageView.prefHeight(200);
        vbox.getChildren().add(0, imageView);
    }

    @Override
    public void setText(String text) {
        TextArea textArea = new TextArea();
        textArea.setText(text);
        textArea.prefWidth(200);
        textArea.prefHeight(200);
        textArea.textProperty().addListener((changeListener) -> {
            presenter.setUserDataText(textArea.getText());
        });
        vbox.getChildren().add(0, textArea);
    }

    @Override
    public void setUserData(UserData userData) {
        presenter.setUserData(userData);
    }

    @Override
    public void removeRepresentationContainer() {
        vbox.getChildren().remove(0);
    }

    @Override
    public int getRepresentationWidth() {
        return 500;
    }

    @Override
    public int getRepresentationHeight() {
        return 300;
    }

    @FXML
    private void setFileButtonAction(ActionEvent ae) {
        presenter.readFile();
    }

}
