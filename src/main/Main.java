package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;


/**
 * @author Elias Adams-Roberts
 * Student ID: 000343429
 * For WGU Capstone
 * Main class for the application
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("..\\view\\loginForm.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args){
        JDBC.openConnection(); // Opens DB connection
//        JDBC.closeConnection(); // Closes DB Connection

        launch(args);
    }
}
