package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Elias Adams-Roberts
 * Contains all methods having to do with appointment model
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customer_id;
    private int user_id;

    public Appointment(int id, String title, String description, String location, String type, Timestamp start, Timestamp end, int customer_id, int user_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer_id = customer_id;
        this.user_id = user_id;
    }

    /**
     * Get appointment id
     * @return appointment id
     */
    public int getId() {
        return id;
    }

    /**
     * Set appt id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get appt title
     * @return appt title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set appt title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get appointment description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set appt description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get appointment location
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set appt location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get appointment type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * get appt start date
     * @return start date
     */
    public LocalDate getStartDate() {
        return start.toLocalDateTime().toLocalDate();
    }

    /**
     * Get start time of appointment
     * @return start time
     */
    public LocalTime getStartTime() {
        return start.toLocalDateTime().toLocalTime();
    }

    /**
     * Set appointment type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get start time of appointment
     * @return start time
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Convert timestamp to localdatetime
     * @return localdatetime
     */
    public java.time.LocalDateTime getStartLocalDateTime() {
        return start.toLocalDateTime();
    }

    /**
     * Get end time of appt
     * @return end time
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Set end time for appt
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * Get customer ID for appt
     * @return customer Id
     */
    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * Get user Id for appt
     * @return user id
     */
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
