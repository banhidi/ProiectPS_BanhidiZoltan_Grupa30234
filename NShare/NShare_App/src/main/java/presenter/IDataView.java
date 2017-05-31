package presenter;

import dataModel.UserData;
import javafx.scene.image.Image;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Created by banhidi on 5/30/2017.
 */
public interface IDataView {

    File showFileChooser();
    void showProgressBox(String title);
    void closeProgressBox();
    void showErrorMessage(String msg);
    void setDateTime(LocalDate d);

    void setDescription(String description);
    String getDescription();

    String getTypeItem();
    void setTypeItem(String type);

    void setImage(Image img);
    void setText(String text);

    void setUserData(UserData userData);
    void removeRepresentationContainer();

    int getRepresentationWidth();
    int getRepresentationHeight();
}
