package model;

import java.sql.Timestamp;

/**
 * Contains all methods having to do with division model
 */
public class Division {
    private int divisionId;
    private String divisionName;
    private int CountryId;

    public Division(int divisionId, String divisionName, Timestamp createDate,
                    String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        CountryId = countryId;
    }

    public Division(String divisionName) {
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }
}
