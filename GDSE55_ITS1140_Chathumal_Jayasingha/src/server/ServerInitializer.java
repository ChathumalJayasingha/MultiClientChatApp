package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("ServerFXML.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.show();

    }
}
