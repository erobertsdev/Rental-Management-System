package controller;

import javafx.scene.control.Alert;

import java.util.Locale;
import java.util.Objects;

/** Class with helper functions */
abstract public class Helper {

    public static String translateText(String text) {
        // Return translated text
        return null; //
    }

    /** Set Locale
     * @return The locale of the system */
    private static Locale setLocale() {
        Locale locale = Locale.getDefault();
        if (Objects.equals(locale.getLanguage(), "fr")) {
            locale = new Locale("fr", "CA");
        }
        locale = new Locale("en", "US");
        return locale;
    }

    /** Get system language
     * @return system language */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getDisplayLanguage();
    }

    /** Returns system country
     * @return system country */
    public static String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getDisplayCountry();
    }

    /** Returns results from setLocale()
     * @return system locale */
    public static Locale getLocale() {
        return setLocale();
    }

    /** Method to produce error dialog   */
    public static void errorDialog(String error) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
