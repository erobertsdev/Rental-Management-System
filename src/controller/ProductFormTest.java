package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Locale;

import static org.mockito.Mockito.*;

class ProductFormTest {
    @Mock
    Button deleteButton;
    @Mock
    Button exitButton;
    @Mock
    TableColumn<Product, Integer> productIDCol;
    @Mock
    TextField productIDTextField;
    @Mock
    TableColumn<Product, String> productNameCol;
    @Mock
    TextField productNameTextField;
    @Mock
    TableColumn<Product, Double> productPriceCol;
    @Mock
    TextField productPriceTextField;
    @Mock
    Button saveButton;
    @Mock
    TableView<Product> productsTableView;
    @Mock
    ZoneId timezone;
    @InjectMocks
    ProductForm productForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPopulateProductsTableView() throws SQLException {
        productForm.populateProductsTableView();
    }

    @Test
    void testPopulateProductTextFields() {
        productForm.populateProductTextFields();
    }

    @Test
    void testHandleExitButton() throws IOException {
        productForm.handleExitButton(null);
    }

    @Test
    void testHandleSaveButton() throws SQLException {
        productForm.handleSaveButton(null);
    }

    @Test
    void testHandleDeleteButton() throws SQLException {
        productForm.handleDeleteButton(null);
    }

    @Test
    void testHandleClearSelectionButton() {
        productForm.handleClearSelectionButton(null);
    }

    @Test
    void testInitialize() {
        productForm.initialize(null, null);
    }

    @Test
    void testGetLanguage() {
        String result = ProductForm.getLanguage();
        Assertions.assertEquals("en", result);
    }

    @Test
    void testGetCountry() {
        String result = ProductForm.getCountry();
        Assertions.assertEquals("US", result);
    }

    @Test
    void testGetCountryFromId() {
        String result = ProductForm.getCountryFromId(0);
        Assertions.assertEquals("US", result);
    }

    @Test
    void testGetUsernameFromId() {
        String result = ProductForm.getUsernameFromId(0);
        Assertions.assertEquals("admin", result);
    }

    @Test
    void testGetLocale() {
        Locale result = ProductForm.getLocale();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testErrorDialog() {
        ProductForm.errorDialog("error");
    }

    @Test
    void testNoticeDialog() {
        ProductForm.noticeDialog("notice");
    }

    @Test
    void testReportDialog() {
        ProductForm.reportDialog("reportType", "reportBlurb", "reportBody");
    }

    @Test
    void testGetTimeZone() {
        ProductForm.getTimeZone();
    }

    @Test
    void testToLocal() {
        Timestamp result = ProductForm.toLocal(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testToUTC() {
        Timestamp result = ProductForm.toUTC(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testLocalToEST() {
        Timestamp result = ProductForm.localToEST(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testMainCheck() {
        boolean result = ProductForm.mainCheck(null, null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAddAppointmentCheck() throws SQLException {
        boolean result = ProductForm.addAppointmentCheck(null, null, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUpdateAppointmentCheck() throws SQLException {
        boolean result = ProductForm.updateAppointmentCheck(0, null, null, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testOverlapCheck() {
        boolean result = ProductForm.overlapCheck(LocalDateTime.of(2022, Month.JUNE, 25, 5, 37, 3), LocalDateTime.of(2022, Month.JUNE, 25, 5, 37, 3), LocalDateTime.of(2022, Month.JUNE, 25, 5, 37, 3), LocalDateTime.of(2022, Month.JUNE, 25, 5, 37, 3), 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCheckSpecialCharacters() {
        boolean result = ProductForm.checkSpecialCharacters("str");
        Assertions.assertEquals(true, result);
    }
}