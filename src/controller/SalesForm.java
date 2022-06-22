package controller;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Customer;
import model.Sale;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class SalesForm extends Helper implements Initializable {
    @FXML private ComboBox customerCombo;
    @FXML private ComboBox productCombo;
    @FXML private TableView<Sale> salesTableView;
    @FXML private TableColumn<Sale, Integer> productIDCol;
    @FXML private TableColumn<Sale, String> productNameCol;
    @FXML private TableColumn<Sale, Double> productPriceCol;
    @FXML private TableColumn<Sale, String> soldByCol;
    @FXML private Label totalCostLabel;

    /** Method to add a sale to the database when the sell button is clicked */
    // Check that customer and product are selected before adding sale
    public void handleSellButton(ActionEvent event) throws SQLException {
        String productString = (String) productCombo.getSelectionModel().getSelectedItem();
        String customerString = (String) customerCombo.getSelectionModel().getSelectedItem();
        if (customerCombo.getValue() != null && productCombo.getValue() != null) {
            // Get current user's ID
            int userID = JDBC.getUserId(LoginForm.currentUser);
            JDBC.addSale(JDBC.getProductPrice(productString), JDBC.getCustomerId(customerString), userID, JDBC.getProductId(productString), productString);
        }
    }

    /** Get selected customer's sales and display them in the table */
    // TODO: Fix getSalesByCustomer method to return ObservableList<Sale>
    public void handleSelectCustomer(ActionEvent event) throws SQLException {
        String customerString = (String) customerCombo.getSelectionModel().getSelectedItem();
        ObservableList<Sale> sales = JDBC.getSalesByCustomer(customerString);
        System.out.println(sales);
    }

    // Return to customerForm when cancel button is clicked
    public void handleCancelButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/customerForm.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Retrieve price of selected product and set total cost label
    public void handleSelectProduct(ActionEvent event) throws SQLException {
        String productString = (String) productCombo.getSelectionModel().getSelectedItem();
        double productPrice = JDBC.getProductPrice(productString);
        totalCostLabel.setText("$" + productPrice);
    }

    // Handle refund
    public void handleRefundButton(ActionEvent event) throws IOException {
        System.out.println("Refund added");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customerNames = null;
        ObservableList<String> productNames = null;
        try {
            customerNames = JDBC.getCustomerNames();
            productNames = JDBC.getProductNames();
        } catch (SQLException e) {
            e.printStackTrace();
            Helper.errorDialog("Error retrieving customers/products from database. Please try again.");
        }
        customerCombo.setItems(customerNames);
        productCombo.setItems(productNames);
    }
}
