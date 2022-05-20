package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditCustomerForm extends Helper implements Initializable {
    @FXML private static TextField customerIdTextField;
    @FXML private static TextField customerNameTextField;
    @FXML private static TextField customerPhoneTextField;
    @FXML private static TextField customerStreetTextField;
    @FXML private static TextField customerPostalTextField;
    private static int customerId;
    // TODO: get these boxes working
    @FXML private ComboBox countryCombo;
    @FXML private ComboBox stateCombo;
    private final Customer selectedCustomer = CustomerForm.getSelectedCustomer();

    private static void handleSaveButton() throws Exception {
        // TODO: Implement check for new customer or updating customer
        customerId = Integer.parseInt(customerIdTextField.getText());
        String name = customerNameTextField.getText();
        String address = customerStreetTextField.getText();
        String postal = customerPostalTextField.getText();
        String phone = customerPhoneTextField.getText();
        // TODO: get division ID from comboBox
        int division = 1; // 1 for testing


        // Implement Input Validation

        // Run UPDATE on DB if editing user, INSERT if creating
        JDBC.addCustomer(customerId, name, address, postal, phone, division);
        // Return to CustomerForm

    }

    private void handleCancelButton(ActionEvent event) throws IOException {
        // Return to CustomerForm
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("..\\view\\customerForm.fxml"))));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Customer: " + selectedCustomer);
        if (selectedCustomer != null) {
            customerIdTextField.setText(Integer.toString(selectedCustomer.getId()));
            customerNameTextField.setText(selectedCustomer.getName());
            customerPhoneTextField.setText(selectedCustomer.getPhoneNumber());
            customerStreetTextField.setText(selectedCustomer.getAddress());
            customerPostalTextField.setText(selectedCustomer.getPostalCode());
            // TODO: Initialize combo boxes
//            customerDivision.setText(Integer.toString(selectedCustomer.getDivision()));


        }

    }
}
