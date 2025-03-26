import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { CalendarEvent } from "../model/model";
import { Observable } from "rxjs";

@Injectable()
export class EventService {

    private http = inject(HttpClient)

    
    getEvents(userId: string): Observable<CalendarEvent[]> {
        return this.http.get<CalendarEvent[]>(`/api/events/${userId}`)
    }

   
    createEvent(eventData: any): Observable<any> {
        return this.http.post('/api/events', eventData)
    }

    getEventDetails(userId: string, date: string): Observable<any> {   
        return this.http.get(`/api/events/${userId}/${date}`);
      }

    deleteEvent(eventId: string, userId: string): Observable<any> {
        return this.http.delete(`/api/events/${userId}/${eventId}`)
    }
}
