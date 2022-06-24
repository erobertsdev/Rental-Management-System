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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductForm extends Helper implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TextField productIDTextField;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TableView<Product> productsTableView;

    /** Method to populate the table with all products */
    public void populateProductsTableView() throws SQLException {
        ObservableList<Product> products = JDBC.getProducts();
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productsTableView.setItems(products);
    }

    // TODO: THIS DOESN'T PLUG INTO ANYTHING
    /** Populate productIDTextField, productNameTextField, and productPriceTextField with the selected product's information */
    public void populateProductTextFields() {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        if (product != null) {
            productIDTextField.setText(Integer.toString(product.getProductID()));
            productNameTextField.setText(product.getProductName());
            productPriceTextField.setText(Double.toString(product.getProductPrice()));
        }
    }

    public void handleExitButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerForm.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate productsTableView
        try {
            populateProductsTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
