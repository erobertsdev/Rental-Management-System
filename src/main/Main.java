package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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

        // Create resource bundle object
//        ResourceBundle rb = ResourceBundle.getBundle("../RBMain/RBMain", Locale.getDefault());
//
//        if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr"))
//            System.out.println(rb.getString("title"));

        // Test FRENCH
        // TODO: Need to fix ZoneID on FXML Form, displays incorrectly
//        Locale.setDefault(new Locale("fr", "CA"));
        launch(args);
    }
}
