package view;

import dataModel.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import presenter.IImageView;
import presenter.ImagePresenter;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by banhidi on 5/31/2017.
 */
public class ImageView implements IImageView {

    public javafx.scene.image.ImageView myImageView;
    private ProgressBox progressBox;
    private ImagePresenter presenter;
    @FXML private TextField descriptionField;
    @FXML private DatePicker datePicker;

    public void initialize() {
        presenter = new ImagePresenter(this);
    }

    @FXML
    private void setFileButtonAction(ActionEvent ae) {
        presenter.chooseFile();
    }

    @FXML
    private void downloadButtonAction(ActionEvent ae) {
        presenter.saveImage();
    }

    @FXML
    private void uploadButtonAction(ActionEvent ae) {
        presenter.uploadFile();
    }

    @Override
    public void setImage(Image img) {
        myImageView.setImage(img);
    }

    @Override
    public File showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(descriptionField.getScene().getWindow());
        return file;
    }

    @Override
    public File directoryChooser() {
        DirectoryChooser d = new DirectoryChooser();
        return d.showDialog(descriptionField.getScene().getWindow());
    }

    @Override
    public String getDescription() {
        return descriptionField.getText();
    }

    @Override
    public void setDescription(String text) {
        descriptionField.setText(text);
    }

    @Override
    public void setDatePicker(LocalDate date) {
        datePicker.setValue(date);
    }

    @Override
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showProgressBox() {
        progressBox = new ProgressBox("Progress", descriptionField.getScene().getWindow());
    }

    @Override
    public void closeProgressBox() {
        if (progressBox != null)
            progressBox.close();
    }

    @Override
    public void close() {
        ((Stage) descriptionField.getScene().getWindow()).close();
    }

    @Override
    public void setUserData(UserData u) {
        presenter.setUserData(u);
    }

    @Override
    public String saveFileDialog() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText("Please introduce the putput file name.");
            Optional<String> o = dialog.showAndWait();
            return o.get();
        } catch(Exception e) {
            return null;
        }
    }

    public void removeButtonAction(ActionEvent actionEvent) {
        presenter.removeFile();
    }
}
