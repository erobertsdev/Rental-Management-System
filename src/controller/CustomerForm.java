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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static helper.JDBC.getAppointmentsById;

/**
 * Contains all methods for dealing with adding and editing customers
 */
public class CustomerForm extends Helper implements Initializable {
    @FXML private TableView<Customer> customersTableview;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, Integer> customerDivisionCol;
    @FXML private TableColumn<Customer, String> customerPostalCol;
    @FXML private TableColumn<Customer, String> customerPhoneCol;
    @FXML private TableColumn<Customer, Boolean> customerVIPCol;
    @FXML private Button deleteCustomerButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private TableView<Appointment> appointmentsTableview;
    @FXML private TableColumn<Appointment, Integer> appointmentIdCol;
    @FXML private TableColumn<Appointment, String> appointmentTitleCol;
    @FXML private TableColumn<Appointment, String> appointmentDescriptionCol;
    @FXML private TableColumn<Appointment, String> appointmentLocationCol;
    @FXML private TableColumn<Appointment, String> appointmentTypeCol;
    @FXML private TableColumn<Appointment, Timestamp> appointmentStartCol;
    @FXML private TableColumn<Appointment, Timestamp> appointmentEndCol;
    @FXML private TableColumn<Appointment, Integer> appointmentCustomerCol;
    @FXML private TableColumn<Appointment, Integer> appointmentUserCol;
    @FXML private DatePicker dateFilter;
    @FXML private RadioButton monthRadio;
    @FXML private RadioButton weekRadio;
    @FXML private CheckBox vipCheckBox;
    @FXML private TextField searchTextField;
    /**
     * The constant selectedCustomer.
     */
    public static Customer selectedCustomer = null;
    /**
     * The constant selectedAppointment.
     */
    public static Appointment selectedAppointment = null;
    /**
     * The constant addingCustomer.
     */
// Differentiates between adding/updating for the customer and appointment editing forms
    public static boolean addingCustomer;
    /**
     * The constant addingAppointment.
     */
    public static boolean addingAppointment;
    @FXML private ChoiceBox<String> reportChoice;

    /**
     * Method to retrieve selected customer
     *
     * @return selected Customer
     */
    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * Method to retrieve selected appointment
     *
     * @return selected appointment
     */
    public static Appointment getSelectedAppointment() { return selectedAppointment; }

    /**
     * Checks if customer has appointments, used to determine if customer can be deleted
     *
     * @param customerId the customer id
     * @return boolean false if customer has appointments
     * @throws SQLException the sql exception
     */
    public boolean checkForAppointments(int customerId) throws SQLException {
        return JDBC.getAppointmentsById(String.valueOf(customerId)).size() != 0;
    }

    /**
     * Method to filter customers tableview by search term
     *
     * @param event the event
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public void handleSearchButton(ActionEvent event) throws IOException, SQLException {
        // get searchTextField value
        String searchTerm = searchTextField.getText();
        if (searchTerm.isEmpty()) {
            // if search term is empty, show all customers
            // throw error if search term is empty
            customersTableview.setItems(JDBC.getCustomers());
        } else {
            // if search term is not empty, show customers that match search term
            customersTableview.setItems(JDBC.getCustomersBySearch(searchTerm));
        }
    }


    /**
     * Method to open Sales Form when sell button is clicked
     *
     * @param event the event
     * @throws IOException the io exception
     */
    public void handleSellButton(ActionEvent event) throws IOException {
        // Get selected customer info
        selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/salesForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to open editCustomer form
     *
     * @param event the event
     * @throws IOException the io exception
     */
    public void handleEditCustomer(ActionEvent event) throws IOException {
        addingCustomer = false;
        try {
            // Throw error if no customer selected
            if (customersTableview.getSelectionModel().getSelectedItem() == null) {
                if (LoginForm.language.equals("fr")) {
                    Helper.errorDialog("Veuillez sélectionner un client à modifier.");
                } else {
                    Helper.errorDialog("Please select a customer to edit.");
                }
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
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Problème lors de l'édition du client. Veuillez réessayer.");
            } else {
                Helper.errorDialog("Problem editing customer. Please try again.");
            }
        }
    }

    /**
     * Method to open add customer form
     *
     * @param event the event
     * @throws IOException the io exception
     */
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

    /**
     * Method to delete customer from DB, checks if customer has appointments first
     *
     * @throws SQLException the sql exception
     */
    public void handleDeleteCustomer() throws SQLException {
        selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
        // Throw error if no customer selected
        if (customersTableview.getSelectionModel().getSelectedItem() == null) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Veuillez sélectionner un client à supprimer.");
            } else {
                Helper.errorDialog("Please select a customer to delete.");
            }
        } else {
            if (checkForAppointments(selectedCustomer.getId())) {
                if (LoginForm.language.equals("fr")) {
                    Helper.errorDialog("Tous les rendez-vous d'un client doivent être supprimés avant que le client puisse être supprimé.");
                } else {
                    Helper.errorDialog("All of a customer's appointments must be deleted before the customer can be deleted.");
                }
            } else {
                // Get selected customer info
                Customer selectedCustomer = customersTableview.getSelectionModel().getSelectedItem();
                // Delete customer from database
                try {
                    JDBC.deleteCustomer(selectedCustomer.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    if (LoginForm.language.equals("fr")) {
                        Helper.errorDialog("Problème lors de la suppression du client. Veuillez réessayer..");
                    } else {
                        Helper.errorDialog("Problem deleting customer. Please try again.");
                    }
                }
                // Refresh tableview
                Helper.errorDialog("Customer ID: " + selectedCustomer.getId() + " with the name " + selectedCustomer.getName() + " successfully deleted.");
                customersTableview.getItems().remove(selectedCustomer);
            }
        }
    }

    /**
     * Method to edit selected appointment
     *
     * @param event the event
     * @throws IOException the io exception
     */
    public void handleEditAppointment(ActionEvent event) throws IOException {
        try {
            // Throw error if no appointment selected
            if (appointmentsTableview.getSelectionModel().getSelectedItem() == null) {
                if (LoginForm.language.equals("fr")) {
                    Helper.errorDialog("Veuillez sélectionner un rendez-vous à modifier.");
                } else {
                    Helper.errorDialog("Please select an appointment to edit.");
                }
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
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Problème d'édition de rendez-vous. Veuillez réessayer.");
            } else {
                Helper.errorDialog("Problem editing appointment. Please try again.");
            }
        }
    }

    /**
     * Method to add appointment, open appointment form
     *
     * @param event the event
     * @throws IOException the io exception
     */
    public void handleAddAppointment(ActionEvent event) throws IOException {
        addingAppointment = true;
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AppointmentForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to delete currently selected appointment
     */
    public void handleDeleteAppointment() {
        // Get selected appointment info
        selectedAppointment = appointmentsTableview.getSelectionModel().getSelectedItem();
        // Throw error if no appointment selected
        if (appointmentsTableview.getSelectionModel().getSelectedItem() == null) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Veuillez sélectionner un rendez-vous à supprimer.");
            } else {
                Helper.errorDialog("Please select an appointment to delete.");
            }
        } else {
            // Delete appointment from database
            try {
                JDBC.deleteAppointment(selectedAppointment.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                if (LoginForm.language.equals("fr")) {
                    Helper.errorDialog("Problème de suppression de rendez-vous. Veuillez réessayer.");
                } else {
                    Helper.errorDialog("Problem deleting appointment. Please try again.");
                }
            }
            // Refresh tableview
            // Throw error with deleted appointment ID and Type of appointment
            Helper.noticeDialog("Appointment ID: " + selectedAppointment.getId() + " \nType: " + selectedAppointment.getType() + " \nsuccessfully deleted.");
            appointmentsTableview.getItems().remove(selectedAppointment);
        }
    }

    /**
     * Method to retrieve current user's appointments and populate appointment tableview
     *
     * @throws SQLException the sql exception
     */
    public void handleMyAppointments() throws SQLException {
        weekRadio.setSelected(false);
        monthRadio.setSelected(false);
        customersTableview.getSelectionModel().clearSelection();
        ObservableList<Appointment> myAppointments = JDBC.getUserAppointments();
        populateAppointments(myAppointments);
    }

    /**
     * Method to populate customer tableview with only VIP customers  @throws SQLException the sql exception
     */
    public void handleVIPCheckBox() throws SQLException {
        if (vipCheckBox.isSelected()) {
            ObservableList<Customer> vipCustomers = JDBC.getVIPCustomers();
            populateCustomers(vipCustomers);
        } else {
            ObservableList<Customer> allCustomers = JDBC.getCustomers();
            populateCustomers(allCustomers);
        }
    }

    /**
     * Method to check if user has appointments within 15 minutes
     * @throws SQLException
     */
    private void checkForUpcomingAppointments() throws SQLException {
        boolean hasAppointments = false;
        for (Appointment appointment : JDBC.getUserAppointments()) {
            if (Duration.between(LocalDateTime.now(), appointment.getStart().toLocalDateTime()).toMinutes() <= 15 &&
                    Duration.between(LocalDateTime.now(), appointment.getStart().toLocalDateTime()).toMinutes() >= 0) {
                if (LoginForm.language.equals("fr")) {
                    Helper.noticeDialog("Vous avez un rendez-vous à venir:\n" + "Rendez-vous: " + appointment.getId() + "\nCommence à: " + appointment.getStart());
                } else {
                    Helper.noticeDialog("You have an upcoming appointment:\n" + "Appointment: " + appointment.getId() + "\nStarts at: " + appointment.getStart());
                }
                hasAppointments = true;
                break;
            }
        }
        if (!hasAppointments) {
            if (LoginForm.language.equals("fr")) {
                Helper.noticeDialog("Vous n'avez aucun rendez-vous à venir.");
            } else {
                Helper.noticeDialog("You have no upcoming appointments.");
            }
        }
    }

    /**
     * LAMBDA EXPRESSION is used here to convert appointment start timestamp to a localDateTime which can then be used
     * to check if the appointment occurs in the selected month; eliminating the need for creating a separate variable or conversion after the fact.
     * Retrieve appointments occurring in selected month
     *
     * @throws SQLException the sql exception
     */
    public void handleMonthRadio() throws SQLException {
        Month selectedMonth = dateFilter.getValue().getMonth();
        weekRadio.setSelected(false);
        if (selectedMonth != null) {
            ObservableList<Appointment> appointmentsThisMonth = JDBC.getAppointments().filtered(a -> a.getStart().toLocalDateTime()
                    .getMonth() == selectedMonth);
            populateAppointments(appointmentsThisMonth);
        }
    }

    /**
     * LAMBDA EXPRESSION is used here to convert appointment start timestamp to a localDateTime which can then be used
     * to check if the appointment occurs within the selected week; eliminating the need for creating a separate variable or conversion after the fact.
     * Method to populate appointment tableview with appointments occurring on selected week
     *
     * @throws SQLException the sql exception
     */
    public void handleWeekRadio() throws SQLException {
        WeekFields weekFields = WeekFields.of(Locale.US);
        int selectedWeek = dateFilter.getValue().get(weekFields.weekOfWeekBasedYear());
        monthRadio.setSelected(false);
        if (selectedWeek != 0) {
            ObservableList<Appointment> appointmentsThisWeek = JDBC.getWeekAppointments().filtered(
                    appointment -> appointment.getStart().toLocalDateTime()
                            .get(weekFields.weekOfWeekBasedYear()) == selectedWeek);
            populateAppointments(appointmentsThisWeek);
        }
    }

    /**
     * Handle exit button.
     */
    public void handleExitButton() {
        System.exit(0);
    }

    /**
     * Generate and show report based on user selection
     *
     * @throws SQLException the sql exception
     */
    public void handleReportButton() throws SQLException {
        String reportType = reportChoice.getValue();
        switch (reportType) {
            case "# of Customer Appointments by Type/Month":
                Helper.reportDialog("Customer Appointments", "Number of Customer Appointments by Type and Month.", reportTotalsByTypeAndMonth());
                break;
                // TODO: Replace contact schedules report
            case "Customer Rentals":
                Helper.reportDialog("Customer Rentals", "List of current customer rentals.", createCustomerRentals());
                break;
            case "VIP Customers":
                Helper.reportDialog("VIP Customers", "List of VIP customers.", createVIPCustomers());
                break;
            case "User Schedules":
                Helper.reportDialog("User Schedules", "Schedule for all users in the organization.", createUserSchedule());
                break;
            default:
                Helper.errorDialog("Please choose the type of report to view.");
        }
    }

    /**
     * Method to generate report with total number of customer appointments by type and month report
     *
     * @return String report with the number of customer appointments by type and month
     * @throws SQLException the sql exception
     */
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

    /**
     * Method to generate report with list of current customer rentals
     *
     * @return String report with list of current customer rentals
     * @throws SQLException the sql exception
     */
    public static String createCustomerRentals() throws SQLException {
        String report = "";
        String rentalStrings = "";
        report += "List of current customer rentals:\n";
        String rental = "SELECT * FROM sales ORDER BY Customer_ID";
        PreparedStatement getRentals = JDBC.connection.prepareStatement(rental);
        ResultSet rentalResults = getRentals.executeQuery();
        while (rentalResults.next()) {
            rentalStrings = "Rental ID: " + rentalResults.getString("Sale_ID") + " Customer ID: " +
                    rentalResults.getString("Customer_ID") + " Product Name: " + rentalResults.getString("Product_Name") +
                    " Product ID: " + rentalResults.getString("Product_ID") + " Rental Date: " + rentalResults.getString("Sale_Date") + "\n";
            report += rentalStrings;
        }
        getRentals.close();
        return report;
    }

    /**
     * Method to generate report of all VIP customers @return String report of all VIP customers  @return the string
     *
     * @throws SQLException the sql exception
     */
    public static String createVIPCustomers() throws SQLException {
        String report = "";
        String customerStrings = "";
        report += "List of all VIP customers:\n";
        String customer = "SELECT * FROM customers WHERE is_VIP = 'Yes'";
        PreparedStatement getCustomers = JDBC.connection.prepareStatement(customer);
        ResultSet customerResults = getCustomers.executeQuery();
        while (customerResults.next()) {
            customerStrings = "Customer ID: " + customerResults.getString("Customer_ID") + " Name: " +
                    customerResults.getString("Customer_Name") + " Address: " + customerResults.getString("Address") +
                    " Phone: " + customerResults.getString("Phone") + " VIP: " + customerResults.getString("is_VIP") + "\n";
            report += customerStrings;
        }
        getCustomers.close();
        return report;
    }




    /** Method to generate schedule for each contact in the organization
     * @return String schedule for each contact in the organization */
//    public String createCustomerRentals() throws SQLException {
//        String report = "";
//        ObservableList<String> customers = JDBC.getCustomerNames();
//
//        for (String customer : customers) {
//            String customerID = String.valueOf(JDBC.getCustomerId(customer));
//            report += "Customer Name: " + customer + " (ID: " + customerID + ")\n\n";
//            report += "---------------\n";
//
//            ObservableList<Sale> rentals = JDBC.getSalesByCustomerId(Integer.parseInt(customerID));
//            if(rentals.isEmpty()) {
//                report += "  No rentals for this customer. \n\n";
//            }
//            for (Sale rental : rentals) {
//                report += rental;
//            }
//        }
//        return report;
//    }

    /**
     * Show schedule for each user in the organization
     *
     * @return report String
     * @throws SQLException the sql exception
     */
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
                            "\nStart: " + Timestamp.valueOf(appointment.getStart().toLocalDateTime()) + " " + TimeZone.getDefault().getDisplayName() +
                            "\nEnd: " + Timestamp.valueOf(appointment.getEnd().toLocalDateTime()) + " " + TimeZone.getDefault().getDisplayName() +
                            "\nCustomer ID: " + appointment.getCustomer_id() + "\n";
                }
                report += "\n";
            }
        }
    return report;
    }

    /**
     * Open productForm.fxml when product button is clicked  @param event the event
     */
    public void handleProductsButton(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/productForm.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates appointments tableview
     *
     * @param appointmentList appointments to be listed
     */
    public void populateAppointments(ObservableList<Appointment> appointmentList) {
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        appointmentUserCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        appointmentsTableview.setItems(appointmentList);
    }

    /**
     * Populates customers tableview
     *
     * @param customerList customers to be listed
     */
    public void populateCustomers(ObservableList<Customer> customerList) {
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerVIPCol.setCellValueFactory(new PropertyValueFactory<>("isVIP"));
        customersTableview.setItems(customerList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check for appointments when user logs in
        if (LoginForm.initialLogon) {
            try {
                checkForUpcomingAppointments();
                LoginForm.initialLogon = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Disable delete customer button if user is not admin
        if (!LoginForm.currentUser.equals("admin")) {
            deleteCustomerButton.setDisable(true);
            deleteAppointmentButton.setDisable(true);
        }
        // Set date filter to today's date
        dateFilter.setValue(LocalDate.now());
        addingAppointment = false;
        addingCustomer = false;
        ObservableList<String> reports = FXCollections.observableArrayList("# of Customer Appointments by Type/Month", "Customer Rentals", "VIP Customers", "User Schedules");
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
            customerVIPCol.setCellValueFactory(new PropertyValueFactory<>("isVIP"));
            customersTableview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            customersTableview.setItems(JDBC.getCustomers());
            

            //** LAMBDA EXPRESSION used for populating appointments tableview with the currently selected customer's appointments as well as
            // adding event listeners to each row */
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
