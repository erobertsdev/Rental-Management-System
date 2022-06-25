/**
 * COMMENTED OUT TO PREVENT MOCKITO PACKAGE FROM PREVENTING PROJECT BEING BUILT
 * If you want to test these, Mockito is required.
 * */

//package test.controller;
//
//import controller.EditCustomerForm;
//import javafx.collections.ObservableList;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import model.Customer;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.time.ZoneId;
//import java.util.Locale;
//
//import static org.mockito.Mockito.*;
//
//class EditCustomerFormTest {
//    @Mock
//    TextField customerIdTextField;
//    @Mock
//    TextField customerNameTextField;
//    @Mock
//    TextField customerPhoneTextField;
//    @Mock
//    TextField customerStreetTextField;
//    @Mock
//    TextField customerPostalTextField;
//    @Mock
//    ComboBox<String> countryCombo;
//    @Mock
//    ComboBox<String> stateCombo;
//    @Mock
//    ComboBox<String> VIPCombo;
//    @Mock
//    Label VIPLabel;
//    @Mock
//    ObservableList<String> countryNames;
//    @Mock
//    Customer selectedCustomer;
//    @Mock
//    ZoneId timezone;
//    @InjectMocks
//    EditCustomerForm editCustomerForm;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testCheckInputs() {
//        boolean result = editCustomerForm.checkInputs();
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testHandleSelectCountry() throws SQLException, IOException {
//        editCustomerForm.handleSelectCountry(null);
//    }
//
//    @Test
//    void testHandleSaveButton() throws SQLException, IOException {
//        editCustomerForm.handleSaveButton(null);
//    }
//
//    @Test
//    void testHandleCancelButton() throws IOException {
//        editCustomerForm.handleCancelButton(null);
//    }
//
//    @Test
//    void testInitialize() {
//        when(selectedCustomer.getId()).thenReturn(0);
//        when(selectedCustomer.getName()).thenReturn("getNameResponse");
//        when(selectedCustomer.getAddress()).thenReturn("getAddressResponse");
//        when(selectedCustomer.getDivision()).thenReturn(0);
//        when(selectedCustomer.getPostalCode()).thenReturn("getPostalCodeResponse");
//        when(selectedCustomer.getPhoneNumber()).thenReturn("getPhoneNumberResponse");
//
//        editCustomerForm.initialize(null, null);
//    }
//
//    @Test
//    void testGetLanguage() {
//        String result = EditCustomerForm.getLanguage();
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetCountry() {
//        String result = EditCustomerForm.getCountry();
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetCountryFromId() {
//        String result = EditCustomerForm.getCountryFromId(0);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetUsernameFromId() {
//        String result = EditCustomerForm.getUsernameFromId(0);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testGetLocale() {
//        Locale result = EditCustomerForm.getLocale();
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testErrorDialog() {
//        EditCustomerForm.errorDialog("error");
//    }
//
//    @Test
//    void testNoticeDialog() {
//        EditCustomerForm.noticeDialog("notice");
//    }
//
//    @Test
//    void testReportDialog() {
//        EditCustomerForm.reportDialog("reportType", "reportBlurb", "reportBody");
//    }
//
//    @Test
//    void testGetTimeZone() {
//        EditCustomerForm.getTimeZone();
//    }
//
//    @Test
//    void testToLocal() {
//        Timestamp result = EditCustomerForm.toLocal(null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testToUTC() {
//        Timestamp result = EditCustomerForm.toUTC(null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testLocalToEST() {
//        Timestamp result = EditCustomerForm.localToEST(null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testMainCheck() {
//        boolean result = EditCustomerForm.mainCheck(null, null);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testAddAppointmentCheck() throws SQLException {
//        boolean result = EditCustomerForm.addAppointmentCheck(null, null, 0);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testUpdateAppointmentCheck() throws SQLException {
//        boolean result = EditCustomerForm.updateAppointmentCheck(0, null, null, 0);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testOverlapCheck() {
//        boolean result = EditCustomerForm.overlapCheck(LocalDateTime.of(2022, Month.JUNE, 25, 5, 35, 6), LocalDateTime.of(2022, Month.JUNE, 25, 5, 35, 6), LocalDateTime.of(2022, Month.JUNE, 25, 5, 35, 6), LocalDateTime.of(2022, Month.JUNE, 25, 5, 35, 6), 0);
//        Assertions.assertEquals(true, result);
//    }
//
//    @Test
//    void testCheckSpecialCharacters() {
//        boolean result = EditCustomerForm.checkSpecialCharacters("str");
//        Assertions.assertEquals(true, result);
//    }
//}