package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCustomerForm extends Helper implements Initializable {
    @FXML private TextField customerIdTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField customerPhoneTextField;
    @FXML private TextField customerStreetTextField;
    @FXML private TextField customerPostalTextField;
    // TODO: get these boxes working
    @FXML private ComboBox countryCombo;
    @FXML private ComboBox stateCombo;
    private final Customer selectedCustomer = CustomerForm.getSelectedCustomer();

    private static void handleSaveButton() {
        // Implement Input Validation

        // Run UPDATE on DB

        // Return to CustomerForm

    }

    private static void handleCancelButton() {

        // Return to CustomerForm
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
