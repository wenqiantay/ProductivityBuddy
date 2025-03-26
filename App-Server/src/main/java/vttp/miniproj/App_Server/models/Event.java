package vttp.miniproj.App_Server.models;

import java.time.LocalDate;

public class Event {
    
    private String eventId;
    private String title;
    private String timeFrom;
    private String timeTo;
    private LocalDate eventDate;
    private String userId;

    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTimeFrom() {
        return timeFrom;
    }
    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }
    public String getTimeTo() {
        return timeTo;
    }
    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
    public LocalDate getEventDate() {
        return eventDate;
    }
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    @Override
    public String toString() {
        return "Event [id=" + eventId + ", title=" + title + ", timeFrom=" + timeFrom + ", timeTo=" + timeTo + ", eventDate="
                + eventDate + ", userId=" + userId + "]";
    }
}
