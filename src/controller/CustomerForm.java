package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;

import static helper.JDBC.*;

public class CustomerForm extends Helper implements Initializable {
    @FXML private TableView<Customer> customersTableview;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, Integer> customerDivisionCol;
    @FXML private TableColumn<Customer, String> customerPostalCol;
    @FXML private TableColumn<Customer, String> customerPhoneCol;
    @FXML private Button editCustomerButton;
    @FXML private Button addCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private TableView<Appointment> appointmentsTableview;
    @FXML private TableColumn<Appointment, Integer> appointmentIdCol;
    @FXML private TableColumn<Appointment, String> appointmentTitleCol;
    @FXML private TableColumn<Appointment, String> appointmentDescriptionCol;
    @FXML private TableColumn<Appointment, String> appointmentLocationCol;
    @FXML private TableColumn<Appointment, String> appointmentTypeCol;
    @FXML private TableColumn<Appointment, Timestamp> appointmentStartCol;
    @FXML private TableColumn<Appointment, Timestamp> appointmentEndCol;
    @FXML private TableColumn<Appointment, Integer> appointmentContactCol;
    @FXML private TableColumn<Appointment, Integer> appointmentCustomerCol;
    @FXML private TableColumn<Appointment, Integer> appointmentUserCol;
    @FXML private RadioButton monthRadio;
    @FXML private RadioButton weekRadio;
    public static Customer selectedCustomer = null;
    public static Appointment selectedAppointment = null;
    // Differentiates between adding/updating for the customer and appointment editing forms
    public static boolean addingCustomer;
    public static boolean addingAppointment;
    @FXML private ChoiceBox<String> reportChoice;

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }
    public static Appointment getSelectedAppointment() { return selectedAppointment; }

    /**
     * Method to check if customer has appointments before deleting
     * @return boolean true if customer has appointments
     */
    public boolean checkForAppointments(int customerId) throws SQLException {
        return JDBC.getAppointmentsById(String.valueOf(customerId)).size() != 0;
    }

    /** Method to open edit customer form */
    public void handleEditCustomer(ActionEvent event) throws IOException {
        try {
            // Throw error if no customer selected
            if (customersTableview.getSelectionModel().getSelectedItem() == null) {
                Helper.errorDialog("Please select a customer to edit.");
            } else {
            // Get selected customer info
                selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/editCustomerForm.fxml")));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e){
            e.printStackTrace();
            Helper.errorDialog("Problem editing customer. Please try again.");
        }
    }

    /** Method to open add customer form */
    public void handleAddCustomer(ActionEvent event) throws IOException {
        // Inform EditCustomerForm that customer is being added not updated
        addingCustomer = true;
        // Open edit customer dialog
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/editCustomerForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /** Method to delete customer, checks if customer has appointments before deletion */
    public void handleDeleteCustomer() throws SQLException {
        selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
        // Throw error if no customer selected
        if (customersTableview.getSelectionModel().getSelectedItem() == null) {
            Helper.errorDialog("Please select a customer to delete.");
        } else {
            if (checkForAppointments(selectedCustomer.getId())) {
                Helper.errorDialog("All of a customer's appointments must be deleted before the customer can be deleted.");
            } else {
                // Get selected customer info
                Customer selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
                // Delete customer from database
                try {
                    JDBC.deleteCustomer(selectedCustomer.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    Helper.errorDialog("Problem deleting customer. Please try again.");
                }
                // Refresh tableview
                Helper.errorDialog("Customer ID: " + selectedCustomer.getId() + " with the name " + selectedCustomer.getName() + " successfully deleted.");
                customersTableview.getItems().remove(selectedCustomer);
            }
        }
    }

    /** Method to edit selected appointment and open appointment form */
    public void handleEditAppointment(ActionEvent event) throws IOException {
        try {
            // Throw error if no appointment selected
            if (appointmentsTableview.getSelectionModel().getSelectedItem() == null) {
                Helper.errorDialog("Please select an appointment to edit.");
            } else {
                // Get selected appointment info
                selectedAppointment = appointmentsTableview.getSelectionModel().getSelectedItem();
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AppointmentForm.fxml")));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e){
            e.printStackTrace();
            Helper.errorDialog("Problem editing appointment. Please try again.");
        }
    }

    /** Method to open appointmentForm to add appointment */
    public void handleAddAppointment(ActionEvent event) throws IOException {
        addingAppointment = true;
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AppointmentForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /** Method to delete currently selected appointment */
    public void handleDeleteAppointment() {
        // Get selected appointment info
        selectedAppointment = appointmentsTableview.getSelectionModel().getSelectedItem();
        // Throw error if no appointment selected
        if (appointmentsTableview.getSelectionModel().getSelectedItem() == null) {
            Helper.errorDialog("Please select an appointment to delete.");
        } else {
            // Delete appointment from database
            try {
                JDBC.deleteAppointment(selectedAppointment.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                Helper.errorDialog("Problem deleting appointment. Please try again.");
            }
            // Refresh tableview
            // Throw error with deleted appointment ID and Type of appointment
            Helper.noticeDialog("Appointment ID: " + selectedAppointment.getId() + " \nType: " + selectedAppointment.getType() + " \nsuccessfully deleted.");
            appointmentsTableview.getItems().remove(selectedAppointment);
        }
    }

    /** Method to populate appointments tableview with current user's appointments */
    public void handleMyAppointments() throws SQLException {
        weekRadio.setSelected(false);
        monthRadio.setSelected(false);
        customersTableview.getSelectionModel().clearSelection();
        ObservableList<Appointment> myAppointments = JDBC.getUserAppointments();
        populateAppointments(myAppointments);
    }

    /** Method to check for appointments occurring within the next 15 minutes */
    private void checkForUpcomingAppointments() throws SQLException {
        for (Appointment appointment : JDBC.getUserAppointments()) {
            System.out.println(Duration.between(LocalDateTime.now(), appointment.getStart().toLocalDateTime()).toMinutes());
            if (Duration.between(LocalDateTime.now(), appointment.getStart().toLocalDateTime()).toMinutes() <= 15 &&
                    Duration.between(LocalDateTime.now(), appointment.getStart().toLocalDateTime()).toMinutes() >= 0) {
                Helper.noticeDialog("You have an upcoming appointment:\n" + "Appointment: " + appointment.getId() + "\nStarts at: " + appointment.getStart());
            }
            else {
                Helper.noticeDialog("You have no upcoming appointments.");
            }
        }
    }



    // TODO: Change these to filtered lists
    /** Method to populate appointments tableview with all appointments occurring this month */
    public void handleMonthRadio() throws SQLException {
        weekRadio.setSelected(false);
        ObservableList<Appointment> appointmentsThisMonth = JDBC.getMonthAppointments();
        populateAppointments(appointmentsThisMonth);
    }

    /** Method to populate appointments tableview with all appointments occurring this week */
    public void handleWeekRadio() throws SQLException {
        monthRadio.setSelected(false);
        ObservableList<Appointment> appointmentsThisWeek = JDBC.getWeekAppointments();
        populateAppointments(appointmentsThisWeek);
    }

    /** Show type of report based on user's choice */
    public void handleReportButton() throws SQLException {
        String reportType = reportChoice.getValue();
        switch (reportType) {
            case "# of Customer Appointments by Type/Month":
                Helper.reportDialog("Customer Appointments", "Number of Customer Appointments by Type and Month.", reportTotalsByTypeAndMonth());
                break;
            case "Contact Schedules":
                Helper.reportDialog("Contact Schedules", "Schedule for each contact in the organization.", createContactSchedule());
                break;
            case "User Schedules":
                Helper.reportDialog("User Schedules", "Schedule for all users in the organization.", createUserSchedule());
                break;
            default:
                Helper.errorDialog("Please choose the type of report to view.");
        }
    }

    // TODO: Make this run when selected from dropdown
    /** Method to generate report with total number of customer appointments by type and month report
     * @return String report with the number of customer appointments by type and month */
    public static String reportTotalsByTypeAndMonth() throws SQLException {
        String report = "";
        String typeStrings = "";
        String monthStrings = "";
        report += "Total number of customer appointments by type and month:\n";
        String type = "SELECT Type, COUNT(Type) as \"Total\" FROM appointments GROUP BY Type";
        PreparedStatement getTypes = JDBC.connection.prepareStatement(type);
        String month = "SELECT MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month";
        PreparedStatement getMonths = JDBC.connection.prepareStatement(month);

        ResultSet typeResults = getTypes.executeQuery();
        ResultSet monthResults = getMonths.executeQuery();

        while (typeResults.next()) {
            typeStrings = "Type: " + typeResults.getString("Type") + " Count: " +
                    typeResults.getString("Total") + "\n";
            report += typeStrings;
        }

        while (monthResults.next()) {
            monthStrings = "Month: " + monthResults.getString("Month") + " Count: " +
                    monthResults.getString("Total") + "\n";
            report += monthStrings;

        }
        getMonths.close();
        getTypes.close();
        return report;
    }

    /** Method to generate schedule for each contact in the organization
     * @return String schedule for each contact in the organization */
    public String createContactSchedule() throws SQLException {
        String report = "";
        ObservableList<String> contacts = JDBC.getContactNames();

        for (String contact : contacts) {
            String contactID = String.valueOf(JDBC.getContactId(contact));
            report += "Contact Name: " + contact + " (ID: " + contactID + ")\n\n";
            report += "---------------\n";

            ObservableList<String> appointments = JDBC.contactAppointmentsById(contactID);
            if(appointments.isEmpty()) {
                report += "  No appointments for contact \n\n";
            }
            for (String appointment : appointments) {
                report += appointment;
            }
        }
        return report;
    }

    /** Method to show schedule for each USER in the organization */
    public String createUserSchedule() throws SQLException {
        String report = "Schedule of all users in organization: \n";
        for (User user: JDBC.getAllUsers()) {
            report += "\n\nUser: " + user.getUserName() + "\n";
            for (Appointment appointment: JDBC.getAppointments()) {
                if (appointment.getUser_id() == user.getId()) {
                    report += "Appointment ID: " + appointment.getId() +
                            "\nTitle: " + appointment.getTitle() +
                            "\nType: " + appointment.getType() +
                            "\nDescription: " + appointment.getDescription() +
                            "\nStart: " + appointment.getStart() +
                            "\nEnd: " + appointment.getEnd() +
                            "\nCustomer ID: " + appointment.getCustomer_id() + "\n";
                }
                report += "\n";
            }
        }
    return report;
    }

    public void populateAppointments(ObservableList<Appointment> appointmentList) {
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
        appointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        appointmentUserCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        appointmentsTableview.setItems(appointmentList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check for appointments when user logs in
        try {
            checkForUpcomingAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addingAppointment = false;
        addingCustomer = false;
        ObservableList<String> reports = FXCollections.observableArrayList("# of Customer Appointments by Type/Month", "Contact Schedules", "User Schedules");
        reportChoice.setItems(reports);
        // Fill Customers Table
        try {
            customersTableview.getItems().setAll(JDBC.getCustomers());
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
            customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            customersTableview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            customersTableview.setItems(JDBC.getCustomers());
            

            // ****** LAMBDA EXPRESSION used for populating appointments tableview with the currently selected customer's appointments as well as
            // adding event listeners to each row *******/
            customersTableview.setRowFactory(tv -> {
                TableRow<Customer> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    // Deselect radio buttons if selected
                    weekRadio.setSelected(false);
                    monthRadio.setSelected(false);
                    // check for non-empty rows
                    if (!row.isEmpty()) {
                        Customer element = row.getItem();
                        int col = element.getId();
                        try {
                            appointmentsTableview.getItems().setAll(getAppointmentsById(String.valueOf(col)));
                            appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                            appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
                            appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                            appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
                            appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                            appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
                            appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
                            appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contact_id"));
                            appointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
                            appointmentUserCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            });

        } catch (SQLException e) {
            Helper.errorDialog("Problem retrieving customers. Please try again.");
        }
    }
}
