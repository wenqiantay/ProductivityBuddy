package vttp.miniproj.App_Server.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.miniproj.App_Server.Repository.EventRepository;
import vttp.miniproj.App_Server.models.Event;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;

    public void addEvent(Event event, String userId){
        event.setUserId(userId);
        eventRepo.save(event);
    }

    public List<Event> getEventsForDate(String userid, LocalDate date){
        return eventRepo.findByUserIdAndDate(userid, date);
    }

    public void deleteEvent(String eventId, String userId) {
        eventRepo.deleteById(eventId, userId);
    }
    
}
