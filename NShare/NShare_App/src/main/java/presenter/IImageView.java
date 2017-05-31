package presenter;

import dataModel.UserData;
import javafx.scene.image.Image;

import java.io.File;
import java.time.LocalDate;

/**
 * Created by banhidi on 5/31/2017.
 */
public interface IImageView {

    void setImage(Image img);
    File showFileChooser();
    File directoryChooser();
    String getDescription();
    void setDescription(String text);
    void setDatePicker(LocalDate date);
    void showErrorMessage(String message);
    void showProgressBox();
    void closeProgressBox();
    void close();
    void setUserData(UserData u);
    String saveFileDialog();

}
