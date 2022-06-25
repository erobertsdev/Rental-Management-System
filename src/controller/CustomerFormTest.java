package controller;

import javafx.scene.control.*;
import model.Appointment;
import model.Customer;
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

class CustomerFormTest {
    @Mock
    TableView<Customer> customersTableview;
    @Mock
    TableColumn<Customer, Integer> customerIdCol;
    @Mock
    TableColumn<Customer, String> customerNameCol;
    @Mock
    TableColumn<Customer, String> customerAddressCol;
    @Mock
    TableColumn<Customer, Integer> customerDivisionCol;
    @Mock
    TableColumn<Customer, String> customerPostalCol;
    @Mock
    TableColumn<Customer, String> customerPhoneCol;
    @Mock
    TableColumn<Customer, Boolean> customerVIPCol;
    @Mock
    Button deleteCustomerButton;
    @Mock
    Button deleteAppointmentButton;
    @Mock
    TableView<Appointment> appointmentsTableview;
    @Mock
    TableColumn<Appointment, Integer> appointmentIdCol;
    @Mock
    TableColumn<Appointment, String> appointmentTitleCol;
    @Mock
    TableColumn<Appointment, String> appointmentDescriptionCol;
    @Mock
    TableColumn<Appointment, String> appointmentLocationCol;
    @Mock
    TableColumn<Appointment, String> appointmentTypeCol;
    @Mock
    TableColumn<Appointment, Timestamp> appointmentStartCol;
    @Mock
    TableColumn<Appointment, Timestamp> appointmentEndCol;
    @Mock
    TableColumn<Appointment, Integer> appointmentCustomerCol;
    @Mock
    TableColumn<Appointment, Integer> appointmentUserCol;
    @Mock
    DatePicker dateFilter;
    @Mock
    RadioButton monthRadio;
    @Mock
    RadioButton weekRadio;
    @Mock
    CheckBox vipCheckBox;
    @Mock
    TextField searchTextField;
    @Mock
    Customer selectedCustomer;
    @Mock
    Appointment selectedAppointment;
    @Mock
    ChoiceBox<String> reportChoice;
    @Mock
    ZoneId timezone;
    @InjectMocks
    CustomerForm customerForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckForAppointments() throws SQLException {
        boolean result = customerForm.checkForAppointments(0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHandleSearchButton() throws SQLException, IOException {
        customerForm.handleSearchButton(null);
    }

    @Test
    void testHandleSellButton() throws IOException {
        customerForm.handleSellButton(null);
    }

    @Test
    void testHandleEditCustomer() throws IOException {
        customerForm.handleEditCustomer(null);
    }

    @Test
    void testHandleAddCustomer() throws IOException {
        customerForm.handleAddCustomer(null);
    }

    @Test
    void testHandleDeleteCustomer() throws SQLException {
        when(selectedCustomer.getId()).thenReturn(0);
        when(selectedCustomer.getName()).thenReturn("getNameResponse");

        customerForm.handleDeleteCustomer();
    }

    @Test
    void testHandleEditAppointment() throws IOException {
        customerForm.handleEditAppointment(null);
    }

    @Test
    void testHandleAddAppointment() throws IOException {
        customerForm.handleAddAppointment(null);
    }

    @Test
    void testHandleDeleteAppointment() {
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getType()).thenReturn("getTypeResponse");

        customerForm.handleDeleteAppointment();
    }

    @Test
    void testHandleMyAppointments() throws SQLException {
        customerForm.handleMyAppointments();
    }

    @Test
    void testHandleVIPCheckBox() throws SQLException {
        customerForm.handleVIPCheckBox();
    }

    @Test
    void testHandleMonthRadio() throws SQLException {
        when(selectedAppointment.getStart()).thenReturn(null);

        customerForm.handleMonthRadio();
    }

    @Test
    void testHandleWeekRadio() throws SQLException {
        when(selectedAppointment.getStart()).thenReturn(null);

        customerForm.handleWeekRadio();
    }

    @Test
    void testHandleExitButton() {
        customerForm.handleExitButton();
    }

    @Test
    void testHandleReportButton() throws SQLException {
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getTitle()).thenReturn("getTitleResponse");
        when(selectedAppointment.getDescription()).thenReturn("getDescriptionResponse");
        when(selectedAppointment.getType()).thenReturn("getTypeResponse");
        when(selectedAppointment.getStart()).thenReturn(null);
        when(selectedAppointment.getEnd()).thenReturn(null);
        when(selectedAppointment.getCustomer_id()).thenReturn(0);
        when(selectedAppointment.getUser_id()).thenReturn(0);

        customerForm.handleReportButton();
    }

    @Test
    void testReportTotalsByTypeAndMonth() throws SQLException {
        String result = CustomerForm.reportTotalsByTypeAndMonth();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testCreateCustomerRentals() throws SQLException {
        String result = CustomerForm.createCustomerRentals();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testCreateVIPCustomers() throws SQLException {
        String result = CustomerForm.createVIPCustomers();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testCreateUserSchedule() throws SQLException {
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getTitle()).thenReturn("getTitleResponse");
        when(selectedAppointment.getDescription()).thenReturn("getDescriptionResponse");
        when(selectedAppointment.getType()).thenReturn("getTypeResponse");
        when(selectedAppointment.getStart()).thenReturn(null);
        when(selectedAppointment.getEnd()).thenReturn(null);
        when(selectedAppointment.getCustomer_id()).thenReturn(0);
        when(selectedAppointment.getUser_id()).thenReturn(0);

        String result = customerForm.createUserSchedule();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testHandleProductsButton() {
        customerForm.handleProductsButton(null);
    }

    @Test
    void testPopulateAppointments() {
        customerForm.populateAppointments(null);
    }

    @Test
    void testPopulateCustomers() {
        customerForm.populateCustomers(null);
    }

    @Test
    void testInitialize() {
        when(selectedCustomer.getId()).thenReturn(0);
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getStart()).thenReturn(null);

        customerForm.initialize(null, null);
    }

    @Test
    void testGetLanguage() {
        String result = CustomerForm.getLanguage();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetCountry() {
        String result = CustomerForm.getCountry();
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetCountryFromId() {
        String result = CustomerForm.getCountryFromId(0);
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetUsernameFromId() {
        String result = CustomerForm.getUsernameFromId(0);
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testGetLocale() {
        Locale result = CustomerForm.getLocale();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testErrorDialog() {
        CustomerForm.errorDialog("error");
    }

    @Test
    void testNoticeDialog() {
        CustomerForm.noticeDialog("notice");
    }

    @Test
    void testReportDialog() {
        CustomerForm.reportDialog("reportType", "reportBlurb", "reportBody");
    }

    @Test
    void testGetTimeZone() {
        CustomerForm.getTimeZone();
    }

    @Test
    void testToLocal() {
        Timestamp result = CustomerForm.toLocal(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testToUTC() {
        Timestamp result = CustomerForm.toUTC(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testLocalToEST() {
        Timestamp result = CustomerForm.localToEST(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testMainCheck() {
        boolean result = CustomerForm.mainCheck(null, null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAddAppointmentCheck() throws SQLException {
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getStart()).thenReturn(null);
        when(selectedAppointment.getEnd()).thenReturn(null);
        when(selectedAppointment.getCustomer_id()).thenReturn(0);

        boolean result = CustomerForm.addAppointmentCheck(null, null, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUpdateAppointmentCheck() throws SQLException {
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getStart()).thenReturn(null);
        when(selectedAppointment.getEnd()).thenReturn(null);
        when(selectedAppointment.getCustomer_id()).thenReturn(0);

        boolean result = CustomerForm.updateAppointmentCheck(0, null, null, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testOverlapCheck() {
        boolean result = CustomerForm.overlapCheck(LocalDateTime.of(2022, Month.JUNE, 25, 5, 33, 55), LocalDateTime.of(2022, Month.JUNE, 25, 5, 33, 55), LocalDateTime.of(2022, Month.JUNE, 25, 5, 33, 55), LocalDateTime.of(2022, Month.JUNE, 25, 5, 33, 55), 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCheckSpecialCharacters() {
        boolean result = CustomerForm.checkSpecialCharacters("str");
        Assertions.assertEquals(true, result);
    }
}