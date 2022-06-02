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
     * @return Helper.errorDialog();ountry name */
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

    /** Method to produce error dialog   */
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

    public static void generateLocalData() {
        timezone = ZoneId.systemDefault();
    }
    
    /**
     * Method to get the current user's timezone
     * @return timezone
     */
    public static ZoneId getLocalTimezone() {
        return ZoneId.systemDefault();
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

    /************** Methods for Appointment notices/validations ********************/

    // TODO: BROKEN
    public static boolean updateCheck(int id, Timestamp start, Timestamp end, int customer_id) throws SQLException {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();

        LocalTime openTime = LocalTime.of(8,00);
        LocalTime closeTime = LocalTime.of(22,00);

        // Passes the appointment info to the appointmentValidator to perform the checks there
        if(!addAppointmentCheck(start, end, customer_id)) {
            return false;
        }

        FilteredList<Appointment> customerAppointments = JDBC.getAppointments().filtered(
                appointment -> appointment.getCustomer_id() == customer_id);

        for (Appointment appointment : customerAppointments) {
            LocalDateTime aStart = Helper.localToEST(appointment.getStart()).toLocalDateTime();
            LocalDateTime aEnd = Helper.localToEST(appointment.getEnd()).toLocalDateTime();
            if (appointment.getId() != id){
                if(!overlapCheck(appointmentStart, appointmentEnd, aStart, aEnd, appointment.getId())){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean addAppointmentCheck(Timestamp start, Timestamp end, int customer_id) throws SQLException {
        LocalDateTime appointmentStart = Helper.localToEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.localToEST(end).toLocalDateTime();

        if(hoursCheck(start, end)){
            FilteredList<Appointment> customerAppointments = JDBC.getAppointments().filtered(
                    appointment -> appointment.getCustomer_id() == customer_id);
            for (Appointment appointment : customerAppointments) {
                LocalDateTime aStart = Helper.localToEST(appointment.getStart()).toLocalDateTime();
                LocalDateTime aEnd = Helper.localToEST(appointment.getEnd()).toLocalDateTime();
                if(!overlapCheck(appointmentStart, appointmentEnd, aStart, aEnd, appointment.getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hoursCheck(Timestamp start, Timestamp end) {
        LocalDateTime appointmentStart = Helper.toEST(start).toLocalDateTime();
        LocalDateTime appointmentEnd = Helper.toEST(end).toLocalDateTime();
        LocalTime openTime = LocalTime.of(8,00);
        LocalTime closeTime = LocalTime.of(22,00);

        // Validates that the appointment begins during office hours
        if (appointmentStart.toLocalTime().isBefore(openTime) ||
                appointmentStart.toLocalTime().isAfter(closeTime)) {
            Helper.errorDialog("Start time must be within office hours.");
            return false;
        }

        // Validates that the appointment ends during office hours
        if (appointmentEnd.toLocalTime().isBefore(openTime) ||
                appointmentEnd.toLocalTime().isAfter(closeTime)) {
            Helper.errorDialog("End time must be within office hours.");
            return false;
        }

        // Validates that the start occurs before the end
        if (appointmentStart.isAfter(appointmentEnd)) {
            Helper.errorDialog("Start time must be before end time.");
            return false;
        }

        // Validates that the appointment doesn't start on a weekend
        if (appointmentStart.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                appointmentStart.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            Helper.errorDialog("Appointments cannot be scheduled on weekends.");
            return false;
        }
        return true;
    }

    public static boolean overlapCheck(LocalDateTime appointmentStart, LocalDateTime appointmentEnd,
                                       LocalDateTime start, LocalDateTime end, int id) {
        // Same start times
        if (appointmentStart.equals(start)) {
            Helper.errorDialog("This appointment starts at the same time as appointment " + id);
            return false;
        }
        // Same end times
        if (appointmentEnd.equals(end)) {
            Helper.errorDialog("This appointment ends at the same time as appointment " + id);
            return false;
        }
        // Start time overlaps
        if (appointmentStart.isAfter(start) && appointmentStart.isBefore(end)) {
            Helper.errorDialog("The start of this appointment overlaps appointment " + id);
            return false;
        }
        // End time overlaps
        if (appointmentEnd.isAfter(start) && appointmentEnd.isBefore(end)) {
            Helper.errorDialog("The end of this appointment overlaps appointment " + id);
            return false;
        }
        // Full overlap
        if (appointmentStart.isBefore(start) && appointmentEnd.isAfter(end)) {
            Helper.errorDialog("This appointment completely overlaps appointment " + id);
            return false;
        }
        return true;
    }
}


