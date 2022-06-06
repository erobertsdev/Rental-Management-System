package controller;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.Appointment;
import model.Division;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;

/** Class with helper functions */
abstract public class Helper {
    private static ZoneId timezone;

    /** Set Locale
     * @return The locale of the system */
    private static Locale setLocale() {
        Locale locale = Locale.getDefault();
        if (Objects.equals(locale.getLanguage(), "fr")) {
            locale = new Locale("fr", "CA");
        } else {
            locale = new Locale("en", "US");
        }
        return locale;
    }

    /** Get system language
     * @return system language */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage();
    }

    /** Returns system country
     * @return system country */
    public static String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getDisplayCountry();
    }

    /** Get Country name from Country ID
     * @return country name */
    public static String getCountryFromId(int id) {
        return switch (id) {
            case 1 -> "U.S";
            case 2 -> "UK";
            default -> "Canada";
        };
    }

    /** Get user name from User Id
     * @return String username */
    public static String getUsernameFromId(int id) {
        return id == 1 ? "test" : "admin";
    }


    /** Returns results from setLocale()
     * @return system locale */
    public static Locale getLocale() {
        return setLocale();
    }

    /** Method to produce error dialog
     * @param error message to display */
    public static void errorDialog(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error (Erreur)");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    /** Method to produce notice dialog
     * @param notice message to display */
    public static void noticeDialog(String notice) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(notice);
        alert.showAndWait();
    }

    /**
     * Open reports dialog
     * @param reportType
     * @param reportBlurb
     * @param reportBody
     */
    public static void reportDialog(String reportType, String reportBlurb, String reportBody) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Report");
        alert.setHeaderText(reportType);
        alert.setContentText("Click 'Show Details' to view: " + reportBlurb);

        Label label = new Label("Report Output:");

        TextArea textArea = new TextArea(reportBody);
        textArea.setEditable(false); // prevent people from faking reports!
        textArea.setWrapText(false); // prevent reports from getting jumbled from wrapping

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    /**
     * Return timezone of system
     */
    public static void getTimeZone() {
        timezone = ZoneId.systemDefault();
    }

    /**
     * Method to get the current user's timezone
     * @return timezone
     */
    public static ZoneId getLocalTimezone() {
        return timezone;
    }

    /**
     * Convert UTC to local time
     * @param timestamp
     * @return timestamp local time
     */
    public static Timestamp toLocal(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(
                        Helper.getLocalTimezone().getId())).toLocalDateTime());
    }

    /**
     * Convert local time to UTC
     * @param timestamp
     * @return timestamp UTC time
     */
    public static Timestamp toUTC(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(Helper.getLocalTimezone().getId())).withZoneSameInstant(
                ZoneId.of("UTC")).toLocalDateTime());
    }

    /**
     * Convert local time to EST
     * @param timestamp
     * @return
     */
    public static Timestamp localToEST(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(getLocalTimezone().getId())).withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime());
    }

    /************** Methods to check for appointment conflicts when adding or updating an appointment ********************/

    /**
     * Check for appointment overlaps
     * @param start
     * @param end
     * @return boolean false if overlap detected
     */
    public static boolean mainCheck(Timestamp start, Timestamp end) {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();
        LocalTime openTime = LocalTime.of(8,00);
        LocalTime closeTime = LocalTime.of(22,00);

        // Checks that start time is before end time
        if (appointmentStart.isAfter(appointmentEnd)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("L'heure de début doit être antérieure à l'heure de fin.");
            } else {
                Helper.errorDialog("Start time must be before end time.");
            }
            return false;
        }

        // Checks that appointment start falls within office hours
        if (appointmentStart.toLocalTime().isBefore(openTime) ||
                appointmentStart.toLocalTime().isAfter(closeTime)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Le rendez-vous ne peut pas commencer avant les heures de bureau.");
            } else {
                Helper.errorDialog("Appointment cannot start before office hours.");
            }
            return false;
        }

        // Check that appointment end falls between business hours
        if (appointmentEnd.toLocalTime().isBefore(openTime) ||
                appointmentEnd.toLocalTime().isAfter(closeTime)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Le rendez-vous ne peut pas se terminer après les heures de bureau.");
            } else {
                Helper.errorDialog("Appointment cannot end after office hours.");
            }
            return false;
        }

        // Checks that appointment isn't scheduled on a weekend
        if (appointmentStart.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                appointmentStart.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Les rendez-vous ne peuvent pas être programmés le week-end.");
            } else {
                Helper.errorDialog("Appointments cannot be scheduled on weekends.");
            }
            return false;
        } else {
            System.out.println("ELSE STATEMENT IN MAINCHECK"); // never runs
            return true;
        }
    }

    /**
     * Check for appointment overlaps when adding an appointment
     * @param start
     * @param end
     * @param customer_id
     * @return boolean false if overlap detected
     * @throws SQLException
     */
    public static boolean addAppointmentCheck(Timestamp start, Timestamp end, int customer_id) throws SQLException {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();

        if (!mainCheck(start, end)) {
            return false;
        }

        else if (mainCheck(start, end)) {
            FilteredList<Appointment> customerAppointments = JDBC.getAppointments().filtered(
                    appointment -> appointment.getCustomer_id() == customer_id);

            for (Appointment appointment : customerAppointments) {
                LocalDateTime apptStart = Helper.localToEST(appointment.getStart()).toLocalDateTime();
                LocalDateTime apptEnd = Helper.localToEST(appointment.getEnd()).toLocalDateTime();

                if (!overlapCheck(appointmentStart, appointmentEnd, apptStart, apptEnd, appointment.getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check for appointment overlaps when updating an appointment
     * @param id
     * @param start
     * @param end
     * @param customer_id
     * @return boolean false if overlap detected
     * @throws SQLException
     */
    public static boolean updateAppointmentCheck(int id, Timestamp start, Timestamp end, int customer_id) throws SQLException {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();
        boolean passed = mainCheck(start, end); // returns false is main check finds a conflict
        FilteredList<Appointment> customerAppointments = JDBC.getAppointments().filtered(
                appointment -> appointment.getCustomer_id() == customer_id);

        for (Appointment a : customerAppointments) {
            LocalDateTime apptStart = Helper.localToEST(a.getStart()).toLocalDateTime();
            LocalDateTime apptEnd = Helper.localToEST(a.getEnd()).toLocalDateTime();
            if (a.getId() != id){
                if(!overlapCheck(appointmentStart, appointmentEnd, apptStart, apptEnd, a.getId())){
                    passed = false;
                }
            }
            else {
                passed = true;
            }
        }
        return passed;
    }

    /**
     * Check for overlaps in appointments
     * @param appointmentStart
     * @param appointmentEnd
     * @param apptStart
     * @param apptEnd
     * @param apptId
     * @return boolean false if overlap detected
     */
    public static boolean overlapCheck(LocalDateTime appointmentStart, LocalDateTime appointmentEnd,
                                       LocalDateTime apptStart, LocalDateTime apptEnd, int apptId) {

        // Full overlap check
        if (appointmentStart.isBefore(apptStart) && appointmentEnd.isAfter(apptEnd)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Le rendez-vous chevauche le rendez-vous " + apptId + ".");
            } else {
                Helper.errorDialog("Appointment overlaps with appointment " + apptId + ".");
            }
            return false;
        }

        // Partial overlap check
        else if (appointmentStart.isAfter(apptStart) && appointmentStart.isBefore(apptEnd)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Le début du rendez-vous chevauche le rendez-vous " + apptId + ".");
            } else {
                Helper.errorDialog("Start of appointment overlaps with appointment " + apptId + ".");
            }
            return false;
        }

        // Partial overlap check
        else if (appointmentEnd.isAfter(apptStart) && appointmentEnd.isBefore(apptEnd)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("La fin du rendez-vous chevauche le rendez-vous " + apptId + ".");
            } else {
                Helper.errorDialog("End of appointment overlaps with appointment " + apptId + ".");
            }
            return false;
        }

        // Check for same start times
        else if (appointmentStart.equals(apptStart)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Le rendez-vous commence en même temps que le rendez-vous " + apptId + ".");
            } else {
                Helper.errorDialog("Appointment starts at the same time as appointment " + apptId + ".");
            }
            return false;
        }

        // Check for same end times
        else if (appointmentEnd.equals(apptEnd)) {
            if (LoginForm.language.equals("fr")) {
                Helper.errorDialog("Le rendez-vous se termine en même temps que le rendez-vous " + apptId + ".");
            } else {
                Helper.errorDialog("Appointment ends at the same time as appointment " + apptId + ".");
            }
            return false;
        } else {
            return true;
        }
    }
}


