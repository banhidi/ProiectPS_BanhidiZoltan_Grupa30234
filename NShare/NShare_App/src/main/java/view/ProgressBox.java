package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by banhidi on 5/30/2017.
 */
public class ProgressBox {

    private final Stage stage;

    public ProgressBox(String title, Window owner) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        ProgressBar pb = new ProgressBar();
        pb.setPrefWidth(200);
        vbox.getChildren().add(pb);

        Scene scene = new Scene(vbox);

        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public void close() {
        stage.close();

    }

}
