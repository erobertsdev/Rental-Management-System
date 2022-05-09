package controller;

import java.util.Locale;
import java.util.Objects;

/** Class with helper functions */
public class Helper {

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
}
