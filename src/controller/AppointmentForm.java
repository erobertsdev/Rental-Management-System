package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML private TextField appointmentDescriptionTextField;
    @FXML private TextField appointmentIdTextField;
    @FXML private TextField appointmentLocationTextField;
    @FXML private TextField appointmentTitleTextField;
    @FXML private TextField appointmentTypeTextField;
    @FXML private Button cancelButton;
    @FXML private ComboBox<?> contactCombo;
    @FXML private ComboBox<?> customerCombo;
    @FXML private AnchorPane endDate;
    @FXML private DatePicker endDatePicker;
    @FXML private ChoiceBox<String> endHourChoice;
    @FXML private ChoiceBox<String> endMinuteChoice;
    @FXML private Button saveButton;
    @FXML private DatePicker startDatePicker;
    @FXML private ChoiceBox<String> startHourChoice;
    @FXML private ChoiceBox<String> startMinuteChoice;
    @FXML private ComboBox<?> userCombo;
    private final ObservableList<String> hours = FXCollections.observableArrayList();
    private final ObservableList<String> minutes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hour and minute choicebox values
        hours.addAll("0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("0", "15", "30", "45");
        startHourChoice.setItems(hours);
        endHourChoice.setItems(hours);
        startMinuteChoice.setItems(minutes);
        endMinuteChoice.setItems(minutes);

    }
}
