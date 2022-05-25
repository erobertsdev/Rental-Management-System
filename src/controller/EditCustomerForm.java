package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditCustomerForm extends Helper implements Initializable {
    @FXML private TextField customerIdTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField customerPhoneTextField;
    @FXML private TextField customerStreetTextField;
    @FXML private TextField customerPostalTextField;
    @FXML private ComboBox<String> countryCombo;
    @FXML private ComboBox<String> stateCombo;
    public ObservableList<String> countryNames;
    private final Customer selectedCustomer = CustomerForm.getSelectedCustomer();

    public void handleSelectCountry(ActionEvent event) throws IOException, SQLException {
        int countryId = 0;
        String countryString = countryCombo.getSelectionModel().getSelectedItem();
        switch (countryString) {
            case "U.S" -> countryId = 1;
            case "UK" -> countryId = 2;
            case "Canada" -> countryId = 3;
        }
        countryNames = JDBC.getDivisionsById(String.valueOf(countryId));
        stateCombo.setItems(countryNames);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: Populate country/state with selected user's data
        if (selectedCustomer != null) {
            ObservableList<String> countries = FXCollections.observableArrayList("U.S", "UK", "Canada");
            countryCombo.setItems(countries);
            customerIdTextField.setText(Integer.toString(selectedCustomer.getId()));
            customerNameTextField.setText(selectedCustomer.getName());
            customerPhoneTextField.setText(selectedCustomer.getPhoneNumber());
            customerStreetTextField.setText(selectedCustomer.getAddress());
            customerPostalTextField.setText(selectedCustomer.getPostalCode());
        }

    }
}