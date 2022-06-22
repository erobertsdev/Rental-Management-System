package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Contains methods to be used in the login form
 */
public class LoginForm extends Helper implements Initializable {
    @FXML private Label zoneLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button loginButton;
    public static final String language = Helper.getLanguage();
    public static boolean initialLogon = true;
    public static String currentUser = "";

    /**
     * Check username and password to login, displays error if login unsuccessful, logs all login attempts
     * @param event
     * @throws Exception
     */
    @FXML private void handleLogin(ActionEvent event) throws Exception {
    final String userName = usernameTextField.getText();
    final String password = passwordTextField.getText();

    if ((userName.length() != 0) && (password.length() != 0)) {
        // Check DB for user/pw match
        boolean match = JDBC.checkLogin(userName, password);
        if (match) {
            currentUser = userName;
            loginTracker(true);
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("..\\view\\customerForm.fxml"))));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            loginTracker(false);
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

    /**
     * Track login attempts and store in login_activity.txt
     * @param loggedIn
     * @throws Exception
     */
    private void loginTracker(boolean loggedIn) throws Exception {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File("login_activity.txt"), true));
        pw.append("\nLogin attempt: ").append(String.valueOf(ZonedDateTime.of(LocalDateTime.now(), Helper.getLocalTimezone()))).append("\nUsername: ").append(usernameTextField.getText()).append("\nSuccessful: ").append(String.valueOf(loggedIn)).append("\n");
        pw.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getTimeZone();
        if (Helper.getLanguage().equals("fr")) {
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            loginButton.setText("Connexion");
        }
        // Outputs TimeZone information to label
//        zoneLabel.setText("Zone ID: " + Helper.getLocalTimezone().toString() + "\nTimeZone: " + TimeZone.getDefault().getDisplayName());
    }
}
