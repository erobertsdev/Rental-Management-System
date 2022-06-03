package controller;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
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

    /**
     * Returns a Division object specified by ID
     * @param divisionID The ID of the division object to be returned
     * @return division
     */
    public static Division getDivision(int divisionID) throws SQLException {
        ObservableList<Division> divisions = JDBC.getAllDivisions();
        for (Division d : divisions) {
            if (d.getDivisionId() == divisionID) {
                return d;
            }
        }
        return null;
    }

    public static void getTimeZone() {
        timezone = ZoneId.systemDefault();
        System.out.println(timezone);
    }
    
    /**
     * Method to get the current user's timezone
     * @return timezone
     */
    public static ZoneId getLocalTimezone() {
        return timezone;
    }

    /** Method to convert UTC to local time */
    public static Timestamp toLocal(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(
                        Helper.getLocalTimezone().getId())).toLocalDateTime());
    }

    /** Method to convert local time to UTC */
    public static Timestamp toUTC(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(Helper.getLocalTimezone().getId())).withZoneSameInstant(
                ZoneId.of("UTC")).toLocalDateTime());
    }

    /** Method to convert UTC to EST */
    public static Timestamp toEST(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime());
    }

    /** Method to convert EST to UTC */
    public static Timestamp toUTC(Timestamp timestamp, String timezone) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }

    /** Method to convert local time to EST */
    public static Timestamp localToEST(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(Helper.getLocalTimezone().getId())).withZoneSameInstant(
                ZoneId.of("-05:00")).toLocalDateTime());
    }

    /** Method to convert EST to local time */
    public static Timestamp estToLocal(Timestamp timestamp, String timezone) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of(
                        Helper.getLocalTimezone().getId())).toLocalDateTime());
    }

    /************** Methods to check for appointment conflicts when adding or updating an appointment ********************/

    public static boolean mainCheck(Timestamp start, Timestamp end) {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();
        LocalTime openTime = LocalTime.of(8,00);
        LocalTime closeTime = LocalTime.of(22,00);
        boolean passed = true;

        // Checks that start time is before end time
        if (appointmentStart.isAfter(appointmentEnd)) {
            Helper.errorDialog("Start time must be before end time.");
            passed = false;
        }

        // Checks that appointment start falls within office hours
        if (appointmentStart.toLocalTime().isBefore(openTime) ||
                appointmentStart.toLocalTime().isAfter(closeTime)) {
            Helper.errorDialog("Appointment cannot start before office hours.");
            passed = false;
        }

        // Check that appointment end falls between business hours
        if (appointmentEnd.toLocalTime().isBefore(openTime) ||
                appointmentEnd.toLocalTime().isAfter(closeTime)) {
            Helper.errorDialog("Appointment cannot end after office hours.");
            passed = false;
        }

        // Checks that appointment isn't scheduled on a weekend
        if (appointmentStart.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                appointmentStart.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            Helper.errorDialog("Appointments cannot be scheduled on weekends.");
            passed = false;
        }
        return passed;
    }

    public static boolean addAppointmentCheck(Timestamp start, Timestamp end, int customer_id) throws SQLException {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();
        boolean passed = true;

        if(mainCheck(start, end)){
            FilteredList<Appointment> customerAppointments = JDBC.getAppointments().filtered(
                    appointment -> appointment.getCustomer_id() == customer_id);

            for (Appointment a : customerAppointments) {
                LocalDateTime aStart = Helper.localToEST(a.getStart()).toLocalDateTime();
                LocalDateTime aEnd = Helper.localToEST(a.getEnd()).toLocalDateTime();

                if(!overlapCheck(appointmentStart, appointmentEnd, aStart, aEnd, a.getId())) {
                    passed = false;
                }
            }
        }
        return passed;
    }

    public static boolean updateAppointmentCheck(int id, Timestamp start, Timestamp end, int customer_id) throws SQLException {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();
        boolean passed = true;

        if(!mainCheck(start, end)) {
            passed = false;
        }

        FilteredList<Appointment> customerAppointments = JDBC.getAppointments().filtered(
                appointment -> appointment.getCustomer_id() == customer_id);

        for (Appointment a : customerAppointments) {
            LocalDateTime aStart = Helper.localToEST(a.getStart()).toLocalDateTime();
            LocalDateTime aEnd = Helper.localToEST(a.getEnd()).toLocalDateTime();
            if (a.getId() != id){
                if(!overlapCheck(appointmentStart, appointmentEnd, aStart, aEnd, a.getId())){
                    passed = false;
                }
            }
        }
        return passed;
    }

    public static boolean overlapCheck(LocalDateTime appointmentStart, LocalDateTime appointmentEnd,
                                       LocalDateTime apptStart, LocalDateTime apptEnd, int apptId) {
        boolean passed = true;

        // Full overlap check
        if (appointmentStart.isBefore(apptStart) && appointmentEnd.isAfter(apptEnd)) {
            Helper.errorDialog("Appointment overlaps with appointment " + apptId + ".");
            passed = false;
        }

        // Partial overlap check
        if (appointmentStart.isAfter(apptStart) && appointmentStart.isBefore(apptEnd)) {
            Helper.errorDialog("Start of appointment overlaps with appointment " + apptId + ".");
            passed = false;
        }

        // Partial overlap check
        if (appointmentEnd.isAfter(apptStart) && appointmentEnd.isBefore(apptEnd)) {
            Helper.errorDialog("End of appointment overlaps with appointment " + apptId + ".");
            passed = false;
        }

        // Check for same start times
        if (appointmentStart.equals(apptStart)) {
            Helper.errorDialog("Appointment starts at the same time as appointment " + apptId + ".");
            passed = false;
        }

        // Check for same end times
        if (appointmentEnd.equals(apptEnd)) {
            Helper.errorDialog("Appointment ends at the same time as appointment " + apptId + ".");
            passed = false;
        }
        return passed;
    }
}


