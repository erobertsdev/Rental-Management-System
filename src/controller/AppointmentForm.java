package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentForm implements Initializable {

    @FXML
    private TextField appointmentDescriptionTextField;

    @FXML
    private TextField appointmentIdTextField;

    @FXML
    private TextField appointmentLocationTextField;

    @FXML
    private TextField appointmentTitleTextField;

    @FXML
    private TextField appointmentTypeTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<?> contactCombo;

    @FXML
    private ComboBox<?> customerCombo;

    @FXML
    private AnchorPane endDate;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox<?> endHourChoice;

    @FXML
    private ChoiceBox<?> endMinuteChoice;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ChoiceBox<?> startHourChoice;

    @FXML
    private ChoiceBox<?> startMinuteChoice;

    @FXML
    private ComboBox<?> userCombo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
