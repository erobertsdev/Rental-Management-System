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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
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
    private Customer selectedCustomer = CustomerForm.getSelectedCustomer();

    /**
     * Check that all fields are filled out before saving/updating customer
     * @return boolean false if there are empty inputs */
    public boolean checkInputs() {
        return !customerNameTextField.getText().isEmpty() && !customerStreetTextField.getText().isEmpty() && !customerPhoneTextField.getText().isEmpty() &&
                !customerPostalTextField.getText().isEmpty() && countryCombo.getValue() != null && stateCombo.getValue() != null;
    }

    /**
     * Set countries in Country combobox
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void handleSelectCountry(ActionEvent event) throws IOException, SQLException {
        int countryId = 0;
        String countryString = countryCombo.getSelectionModel().getSelectedItem();
        switch (countryString) {
            case "U.S" -> countryId = 1;
            case "UK" -> countryId = 2;
            case "Canada" -> countryId = 3;
        }
        countryNames = JDBC.getDivisionsById(String.valueOf(countryId)); // Retrieve all states in selected country
        stateCombo.setItems(countryNames); // Add appropriate "states" according to selected country
        stateCombo.setValue(null); // Clears the state if a new country is selected
    }

    /**
     * Add or update customer when save button is clicked
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void handleSaveButton(ActionEvent event) throws SQLException, IOException {
        if (!checkInputs()) {
            Helper.errorDialog("All fields are required.");
        } else {
            if (!CustomerForm.addingCustomer) {
                JDBC.updateCustomer(Integer.parseInt(customerIdTextField.getText()), customerNameTextField.getText(), customerStreetTextField.getText(), customerPostalTextField.getText(),
                        customerPhoneTextField.getText(), JDBC.stateIdFromName(stateCombo.getSelectionModel().getSelectedItem()));
                // Return to customer screen
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                // Add customer to DB
                // TODO: Change Created_By from int to currentUser name
                JDBC.addCustomer(customerNameTextField.getText(), customerStreetTextField.getText(), customerPostalTextField.getText(),
                        customerPhoneTextField.getText(), JDBC.stateIdFromName(stateCombo.getSelectionModel().getSelectedItem()));
                CustomerForm.addingCustomer = false;
                selectedCustomer = null;
                // Return to customer screen
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    /**
     * Cancel adding/editing customer and return to customerForm
     * @param event
     * @throws IOException
     */
    public void handleCancelButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (selectedCustomer != null) {
            String customerState = null;
            String customerCountry = null;
            try {
                customerState = JDBC.stateNameFromId(selectedCustomer.getDivision());
                customerCountry = JDBC.countryFromDivisionId(selectedCustomer.getDivision());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ObservableList<String> countries = FXCollections.observableArrayList("U.S", "UK", "Canada");
            countryCombo.setItems(countries);
            customerIdTextField.setText(Integer.toString(selectedCustomer.getId()));
            customerNameTextField.setText(selectedCustomer.getName());
            customerPhoneTextField.setText(selectedCustomer.getPhoneNumber());
            customerStreetTextField.setText(selectedCustomer.getAddress());
            customerPostalTextField.setText(selectedCustomer.getPostalCode());
            countryCombo.setItems(countries);
            countryCombo.setValue(customerCountry);
            stateCombo.setValue(customerState);
        } else {
            customerIdTextField.setText("Auto-Generated");
            ObservableList<String> countries = FXCollections.observableArrayList("U.S", "UK", "Canada");
            countryCombo.setItems(countries);
        }
    }
}