package controller;

import javafx.scene.control.*;
import model.Sale;
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

class SalesFormTest {
    @Mock
    ComboBox customerCombo;
    @Mock
    ComboBox productCombo;
    @Mock
    TableView<Sale> salesTableView;
    @Mock
    TableColumn<Sale, Integer> productIDCol;
    @Mock
    TableColumn<Sale, String> productNameCol;
    @Mock
    TableColumn<Sale, Double> productPriceCol;
    @Mock
    TableColumn<Sale, String> soldByCol;
    @Mock
    TableColumn<Sale, String> saleDateCol;
    @Mock
    TableColumn<Sale, Integer> saleIdCol;
    @Mock
    Button refundButton;
    @Mock
    Label totalCostLabel;
    @Mock
    ZoneId timezone;
    @InjectMocks
    SalesForm salesForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleSellButton() throws SQLException {
        salesForm.handleSellButton(null);
    }

    @Test
    void testHandleSelectCustomer() throws SQLException {
        salesForm.handleSelectCustomer(null);
    }

    @Test
    void testHandleCancelButton() throws IOException {
        salesForm.handleCancelButton(null);
    }

    @Test
    void testHandleSelectProduct() throws SQLException {
        salesForm.handleSelectProduct(null);
    }

    @Test
    void testHandleRefundButton() throws SQLException {
        salesForm.handleRefundButton(null);
    }

    @Test
    void testPopulateSalesTableView() {
        salesForm.populateSalesTableView(null);
    }

    @Test
    void testInitialize() {
        salesForm.initialize(null, null);
    }

    @Test
    void testGetLanguage() {
        String result = SalesForm.getLanguage();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetCountry() {
        String result = SalesForm.getCountry();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetCountryFromId() {
        String result = SalesForm.getCountryFromId(0);
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetUsernameFromId() {
        String result = SalesForm.getUsernameFromId(0);
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetLocale() {
        Locale result = SalesForm.getLocale();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testErrorDialog() {
        SalesForm.errorDialog("error");
    }

    @Test
    void testNoticeDialog() {
        SalesForm.noticeDialog("notice");
    }

    @Test
    void testReportDialog() {
        SalesForm.reportDialog("reportType", "reportBlurb", "reportBody");
    }

    @Test
    void testGetTimeZone() {
        SalesForm.getTimeZone();
    }

    @Test
    void testToLocal() {
        Timestamp result = SalesForm.toLocal(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testToUTC() {
        Timestamp result = SalesForm.toUTC(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testLocalToEST() {
        Timestamp result = SalesForm.localToEST(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testMainCheck() {
        boolean result = SalesForm.mainCheck(null, null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAddAppointmentCheck() throws SQLException {
        boolean result = SalesForm.addAppointmentCheck(null, null, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUpdateAppointmentCheck() throws SQLException {
        boolean result = SalesForm.updateAppointmentCheck(0, null, null, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testOverlapCheck() {
        boolean result = SalesForm.overlapCheck(LocalDateTime.of(2022, Month.JUNE, 25, 5, 38, 38), LocalDateTime.of(2022, Month.JUNE, 25, 5, 38, 38), LocalDateTime.of(2022, Month.JUNE, 25, 5, 38, 38), LocalDateTime.of(2022, Month.JUNE, 25, 5, 38, 38), 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCheckSpecialCharacters() {
        boolean result = SalesForm.checkSpecialCharacters("str");
        Assertions.assertEquals(true, result);
    }
}