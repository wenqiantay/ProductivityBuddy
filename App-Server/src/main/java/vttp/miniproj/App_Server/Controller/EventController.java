package vttp.miniproj.App_Server.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.miniproj.App_Server.models.Event;
import vttp.miniproj.App_Server.services.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventSvc;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        String eventId = UUID.randomUUID().toString();
        event.setEventId(eventId);

        eventSvc.addEvent(event, event.getUserId());
        JsonObject body = Json.createObjectBuilder()
                .add("message", "Event Saved Successfully")
                .build();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{userId}/{date}")
    public ResponseEntity<List<Event>> getEvents(@PathVariable String userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Event> eventList = eventSvc.getEventsForDate(userId, date);
        return ResponseEntity.ok(eventList);
    }

    @DeleteMapping("/{userId}/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String userId, @PathVariable String eventId) {
        eventSvc.deleteEvent(eventId, userId);

        JsonObject body = Json.createObjectBuilder()
                .add("message", "Event deleted successfully.")
                .build();

        return ResponseEntity.ok(body);
    }
}
