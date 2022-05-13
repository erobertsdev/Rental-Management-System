package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginForm extends Helper implements Initializable {
    @FXML private Label zoneLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button loginButton;
    private final String language = Helper.getLanguage();

    @FXML private void handleLogin(ActionEvent event) throws SQLException {
    final String userName = usernameTextField.getText();
    final String password = passwordTextField.getText();

    if ((userName.length() != 0) && (password.length() != 0)) {
        // Check DB for user/pw match
        boolean match = JDBC.checkLogin(userName, password);
        // Match found: TODO: Open Appointments FXML Form
        if (match) {
            System.out.println("Match found: " + userName + " | " + password);
        } else {
            // Shows dialog if no user/pw match found
            if (language.equals("fr")) {
                Helper.errorDialog("Nom d'utilisateur / Mot de passe incorrect. Veuillez vérifier l'orthographe et réessayer.");
            } else {
                Helper.errorDialog("Incorrect username/password. Please check your spelling and try again.");
            }
        }
    } else {
        if (language.equals("fr")) {
            Helper.errorDialog("Le nom d'utilisateur et le mot de passe sont requis.");
        } else {
            Helper.errorDialog("Username and password are required.");
        }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Helper.getLocale());
        if (Helper.getLanguage().equals("fr")) {
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            loginButton.setText("Connexion");
        }
        zoneLabel.setText(Helper.getLocale().toString());
    }
}
