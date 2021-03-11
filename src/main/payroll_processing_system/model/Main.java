package payroll_processing_system.model;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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
             stage.setScene(new Scene(root, 800, 500));
             window.show();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
