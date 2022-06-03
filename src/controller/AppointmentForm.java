package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppointmentForm implements Initializable {

    @FXML private TextField appointmentDescriptionTextField;
    @FXML private TextField appointmentIdTextField;
    @FXML private TextField appointmentLocationTextField;
    @FXML private TextField appointmentTitleTextField;
    @FXML private TextField appointmentTypeTextField;
    @FXML private Button cancelButton;
    @FXML private ComboBox<String> contactCombo;
    @FXML private ComboBox<String> customerCombo;
    @FXML private AnchorPane endDate;
    @FXML private DatePicker endDatePicker;
    @FXML private ChoiceBox<String> endHourChoice;
    @FXML private ChoiceBox<String> endMinuteChoice;
    @FXML private Button saveButton;
    @FXML private DatePicker startDatePicker;
    @FXML private ChoiceBox<String> startHourChoice;
    @FXML private ChoiceBox<String> startMinuteChoice;
    @FXML private ComboBox<String> userCombo;
    private final ObservableList<String> hours = FXCollections.observableArrayList();
    private final ObservableList<String> minutes = FXCollections.observableArrayList();
    private final Appointment selectedAppointment = CustomerForm.getSelectedAppointment();

    /** Method to check that no fields are null
     * @return boolean false if there are empty inputs */
    public boolean checkInputs() {
        return !appointmentDescriptionTextField.getText().isEmpty() && !appointmentLocationTextField.getText().isEmpty() && !appointmentTitleTextField.getText().isEmpty() &&
                !appointmentTypeTextField.getText().isEmpty() && contactCombo.getValue() != null && customerCombo.getValue() != null && startDatePicker.getValue() != null &&
                endDatePicker.getValue() != null && startHourChoice.getValue() != null && startMinuteChoice.getValue() != null && endHourChoice.getValue() != null &&
                endMinuteChoice.getValue() != null;
    }

    /** Method to save appointment */
    public void handleSaveButton(ActionEvent event) throws SQLException, IOException {
        if (!checkInputs()) {
            Helper.errorDialog("All fields are required.");
        } else {

            // Convert start time to timestamp
            Timestamp startTime = Timestamp.valueOf(startDatePicker.getValue().toString() + " " +
            startHourChoice.getValue() + ":" + startMinuteChoice.getValue() + ":00");

            // Convert end time to timestamp
            Timestamp endTime = Timestamp.valueOf(endDatePicker.getValue().toString() + " " +
            endHourChoice.getValue() + ":" + endMinuteChoice.getValue() + ":00");

            // Boolean for scheduling conflicts
            boolean passedChecks; // TODO: GET THESE CHECKS TO WORK
            // Check if adding or updating appointment
            if (!CustomerForm.addingAppointment) {
                    // Check for scheduling issues
                    passedChecks = Helper.updateAppointmentCheck(Integer.parseInt(appointmentIdTextField.getText()), startTime, endTime, JDBC.getCustomerId(customerCombo.getValue()));
                    Helper.errorDialog("Passed checks: " + passedChecks);
                    if (passedChecks) {
                        // Update appointment
                        JDBC.updateAppointment(selectedAppointment.getId(), appointmentTitleTextField.getText(), appointmentDescriptionTextField.getText(), appointmentLocationTextField.getText(),
                                appointmentTypeTextField.getText(), startTime, endTime, JDBC.getCustomerId(customerCombo.getValue()), JDBC.getUserId(userCombo.getValue()), JDBC.getContactId(contactCombo.getValue()));
                        Helper.errorDialog("Appointment Updated Successfully.");
                        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    }
                } else {
                    // Check for scheduling issues
                    passedChecks = Helper.addAppointmentCheck(startTime, endTime, JDBC.getCustomerId(customerCombo.getValue()));
                    Helper.errorDialog("Passed checks: " + passedChecks);
                    if (passedChecks) {
                        // Add appointment to database
                        JDBC.addAppointment(appointmentTitleTextField.getText(), appointmentDescriptionTextField.getText(), appointmentLocationTextField.getText(),
                                appointmentTypeTextField.getText(), startTime, endTime, JDBC.getCustomerId(customerCombo.getValue()), JDBC.getUserId(userCombo.getValue()), JDBC.getContactId(contactCombo.getValue()));
                        Helper.errorDialog("Appointment Added Successfully.");
                        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    }
                }
        }
    }

    /** Method to cancel appointment */
    public void handleCancelButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate Hour and minute choicebox values, filled out when updating AND adding
        hours.addAll("0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("0", "15", "30", "45");
        startHourChoice.setItems(hours);
        endHourChoice.setItems(hours);
        startMinuteChoice.setItems(minutes);
        endMinuteChoice.setItems(minutes);

        // Retrieve all contacts and populate contact combobox
        ObservableList<String> contactNames = null;
        ObservableList<String> customerNames = null;
        ObservableList<String> userNames = null;
        try {
            contactNames = JDBC.getContactNames();
            customerNames = JDBC.getCustomerNames();
            userNames = JDBC.getUserNames();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactCombo.setItems(contactNames);
        customerCombo.setItems(customerNames);
        userCombo.setItems(userNames);

        // Retrieve selected appointment information and populate form when editing appointment
        if (!CustomerForm.addingAppointment) { // Editing existing appointment
            // set inputs with selected appointment's values
            appointmentIdTextField.setText(Integer.toString(selectedAppointment.getId()));
            appointmentTitleTextField.setText(selectedAppointment.getTitle());
            appointmentDescriptionTextField.setText(selectedAppointment.getDescription());
            appointmentLocationTextField.setText(selectedAppointment.getLocation());
            appointmentTypeTextField.setText(selectedAppointment.getType());
            // Populate startDatePicker and endDatePicker with selected appointment's start and end dates
            startDatePicker.setValue(selectedAppointment.getStart().toLocalDateTime().toLocalDate());
            endDatePicker.setValue(selectedAppointment.getEnd().toLocalDateTime().toLocalDate());
            // Populate startHourChoice and endHourChoice with selected appointment's start and end times
            startHourChoice.setValue(selectedAppointment.getStart().toLocalDateTime().toLocalTime().toString().substring(0,2)); // get hour
            startMinuteChoice.setValue(selectedAppointment.getStart().toLocalDateTime().toLocalTime().toString().substring(3,5)); // get minute
            endHourChoice.setValue(selectedAppointment.getEnd().toLocalDateTime().toLocalTime().toString().substring(0,2)); // get hour
            endMinuteChoice.setValue(selectedAppointment.getEnd().toLocalDateTime().toLocalTime().toString().substring(3,5)); // get minute
            
            // set combo boxes
            try {
                contactCombo.setValue(JDBC.getContactName(selectedAppointment.getContact_id()));
                customerCombo.setValue(JDBC.getCustomerName(selectedAppointment.getCustomer_id()));
                userCombo.setValue(JDBC.getCurrentUserName(JDBC.getCurrentUser()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else { // Adding an appointment
            // set combo boxes
            appointmentIdTextField.setText("Auto-Generated");
        }
    }
}
