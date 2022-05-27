package controller;

import com.mysql.cj.xdevapi.Table;
import helper.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

import static helper.JDBC.getAppointmentsById;

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
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, String> apptTitleCol;
    @FXML private TableColumn<Appointment, String> apptDescriptionCol;
    @FXML private TableColumn<Appointment, String> apptLocationCol;
    @FXML private TableColumn<Appointment, String> apptTypeCol;
    @FXML private TableColumn<Appointment, Timestamp> apptStartCol;
    @FXML private TableColumn<Appointment, Timestamp> apptEndCol;
    public static Customer selectedCustomer = null;

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

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

    public void handleAddCustomer(ActionEvent event) throws IOException {
        // Open edit customer dialog
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/editCustomerForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleDeleteCustomer() {
        // Throw error if no customer selected
        if (customersTableview.getSelectionModel().getSelectedItem() == null) {
            Helper.errorDialog("Please select a customer to delete.");
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
            customersTableview.getItems().remove(selectedCustomer);
        }
    }

    public void handleEditAppt() {

    }

    public void handleAddAppt() {

    }

    public void handleDeleteAppt() {

    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            

            /** Event listener to detect when row in Customers tableview is selected and
             * populate the Appointments tableview with that customer's appointments */
            // ****** Lambda function *******
            customersTableview.setRowFactory(tv -> {
                TableRow<Customer> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    // check for non-empty rows
                    if (!row.isEmpty()) {
                        Customer element = row.getItem();
                        int col = element.getId();
                        try {
                            appointmentsTableview.getItems().setAll(getAppointmentsById(String.valueOf(col)));
                            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                            apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
                            apptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                            apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
                            apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                            apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
                            apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
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
