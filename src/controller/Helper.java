package controller;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Division;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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
     * @return String country name */
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
        return timezone;
    }

    /** Method to convert UTC to local time */
    public static Timestamp toLocal(Timestamp timestamp) {
        Timestamp local = Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(
                        Helper.getLocalTimezone().getId())).toLocalDateTime());

        return local;
    }

    /** Method to convert local time to UTC */
    public static Timestamp toUTC(Timestamp timestamp) {
        Timestamp utc = Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(Helper.getLocalTimezone().getId())).withZoneSameInstant(
                ZoneId.of("UTC")).toLocalDateTime());

        return utc;
    }

    /** Method to convert UTC to EST */
    public static Timestamp toEST(Timestamp timestamp) {
        Timestamp est = Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime());

        return est;
    }

    /** Method to convert EST to UTC */
    public static Timestamp toUTC(Timestamp timestamp, String timezone) {
        Timestamp utc = Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

        return utc;
    }

    /** Method to convert local time to EST */
    public static Timestamp toEST(Timestamp timestamp, String timezone) {
        Timestamp est = Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of("America/New_York")).toLocalDateTime());

        return est;
    }

    /** Method to convert EST to local time */
    public static Timestamp estToLocal(Timestamp timestamp, String timezone) {
        Timestamp local = Timestamp.valueOf(timestamp.toLocalDateTime().atZone(
                ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of(
                        Helper.getLocalTimezone().getId())).toLocalDateTime());

        return local;
    }

}


