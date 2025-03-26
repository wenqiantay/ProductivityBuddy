package vttp.miniproj.App_Server.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.miniproj.App_Server.models.Event;

@Repository
public class EventRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_SAVE_EVENT="INSERT INTO events(event_id, user_id, title, time_from, time_to, event_date) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_EVENTS="SELECT * FROM events WHERE user_id = ? AND event_date = ?";
    private static final String SQL_DELETE_EVENT="DELETE FROM events WHERE event_id = ? AND user_id = ?";

    public void save(Event event) {
        jdbcTemplate.update(SQL_SAVE_EVENT, event.getEventId(), event.getUserId(), event.getTitle(), event.getTimeFrom(), event.getTimeTo(), event.getEventDate());
    }

    public List<Event> findByUserIdAndDate(String userId, LocalDate date) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_EVENTS, userId, date);

        List<Event> eventList = new ArrayList<>();

        while(rs.next()) {
            Event event = new Event();
            event.setEventId(rs.getString("event_id"));
            event.setUserId(rs.getString("user_id"));
            event.setTitle(rs.getString("title"));
            event.setTimeFrom(rs.getString("time_from"));
            event.setTimeTo(rs.getString("time_to"));
            event.setEventDate(rs.getDate("event_date").toLocalDate());
            eventList.add(event);
        }

        return eventList;
    }

    public void deleteById(String eventId, String userId) {
        jdbcTemplate.update(SQL_DELETE_EVENT, eventId, userId);
    }
    
}
