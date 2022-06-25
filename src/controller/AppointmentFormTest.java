package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Appointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class AppointmentFormTest {
    @Mock
    TextField appointmentDescriptionTextField;
    @Mock
    TextField appointmentIdTextField;
    @Mock
    TextField appointmentLocationTextField;
    @Mock
    TextField appointmentTitleTextField;
    @Mock
    TextField appointmentTypeTextField;
    @Mock
    Button cancelButton;
    @Mock
    ComboBox<String> contactCombo;
    @Mock
    ComboBox<String> customerCombo;
    @Mock
    AnchorPane endDate;
    @Mock
    DatePicker endDatePicker;
    @Mock
    ChoiceBox<String> endHourChoice;
    @Mock
    ChoiceBox<String> endMinuteChoice;
    @Mock
    Button saveButton;
    @Mock
    DatePicker startDatePicker;
    @Mock
    ChoiceBox<String> startHourChoice;
    @Mock
    ChoiceBox<String> startMinuteChoice;
    @Mock
    ComboBox<String> userCombo;
    @Mock
    ObservableList<String> hours;
    @Mock
    ObservableList<String> minutes;
    @Mock
    Appointment selectedAppointment;
    @InjectMocks
    AppointmentForm appointmentForm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckInputs() {
        boolean result = appointmentForm.checkInputs();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHandleSaveButton() throws SQLException, IOException {
        when(selectedAppointment.getId()).thenReturn(0);

        appointmentForm.handleSaveButton(null);
    }

    @Test
    void testHandleCancelButton() throws IOException {
        appointmentForm.handleCancelButton(null);
    }

    @Test
    void testInitialize() {
        when(selectedAppointment.getId()).thenReturn(0);
        when(selectedAppointment.getTitle()).thenReturn("getTitleResponse");
        when(selectedAppointment.getDescription()).thenReturn("getDescriptionResponse");
        when(selectedAppointment.getLocation()).thenReturn("getLocationResponse");
        when(selectedAppointment.getType()).thenReturn("getTypeResponse");
        when(selectedAppointment.getStart()).thenReturn(null);
        when(selectedAppointment.getEnd()).thenReturn(null);
        when(selectedAppointment.getCustomer_id()).thenReturn(0);

        appointmentForm.initialize(null, null);
    }
}