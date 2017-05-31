package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by banhidi on 5/22/2017.
 */
public class MainClass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        try {
            Parent p = FXMLLoader.load(getClass().getResource("LoginForm.fxml"));
            Scene scene = new Scene(p);
            stage.setTitle("Welcome to NShare");
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            System.out.println("Error when loading FXML file: " + e);
            System.exit(1);
        }
    }

}
