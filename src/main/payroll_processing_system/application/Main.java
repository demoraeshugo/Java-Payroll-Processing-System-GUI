package payroll_processing_system.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage window = stage;
        Parent root;
        try {
             root = FXMLLoader.load(getClass().getResource("../view/home.fxml"));
             stage.setTitle("Payroll Processing");
             stage.setScene(new Scene(root, 900, 600));
             window.show();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
