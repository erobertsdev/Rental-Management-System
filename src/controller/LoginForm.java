package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginForm extends Helper implements Initializable {
    @FXML private Label zoneLabel;
    @FXML private Label errorMessage;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;

    @FXML private void handleLogin(ActionEvent event) {
    final String userName = usernameTextField.getText();
    final String password = passwordTextField.getText();

    if ((userName.length() != 0) && (password.length() != 0)) {
        // Check DB for userName/PW Match

        // Match found: Open Appointments FXML Form

        // NO MATCH: Display error
        errorMessage.setText("Incorrect username/password. Please check your spelling and try again.");

        // Append result to login_activity.txt
    } else {
        errorMessage.setText("Username and password are required.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneLabel.setText(Helper.getLocale().toString());
        System.out.println(Helper.getLanguage());
    }
}
