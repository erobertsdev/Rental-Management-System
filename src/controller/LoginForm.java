package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginForm implements Initializable {
    @FXML
    private Label zoneLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneLabel.setText(Helper.getCountry() + " : " + Helper.getLanguage());
    }
}
