package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

// TODO: Remove FXML loader version to get rid of stupid WARNING
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("..\\view\\loginForm.fxml")));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args){
        JDBC.openConnection(); // Opens DB connection
//        JDBC.closeConnection(); // Closes DB Connection
        launch(args);
    }
}
