package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AppointmentForm {

    @FXML
    private TextField apptDescriptionTextField;

    @FXML
    private TextField apptIdTextField;

    @FXML
    private TextField apptLocationTextField;

    @FXML
    private TextField apptTitleTextField;

    @FXML
    private TextField apptTypeTextField;

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

}
