import { Component, inject } from '@angular/core';
import { CalendarEvent, Day, EventItem } from '../model/model';
import { EventService } from '../services/EventService';
import { UserStore } from '../user.store';


@Component({
  selector: 'app-event-calendar',
  standalone: false,
  templateUrl: './event-calendar.component.html',
  styleUrl: './event-calendar.component.css'
})
export class EventCalendarComponent {
  today = new Date()
  month = this.today.getMonth()
  year = this.today.getFullYear()
  activeDay!: number;
  days: Day[] = []
  months = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ]

  eventsArr: CalendarEvent[] = []
  dayEvents: CalendarEvent[] = []


  gotoDateInput: string = ''

  selectedDayName: string = ''
  selectedDateText: string = ''


  addEventOpen = false

  newEventTitle: string = ''
  newEventTimeFrom: string = ''
  newEventTimeTo: string = ''

  private userStore = inject(UserStore)

  protected userId !: string
  private eventService = inject(EventService)

  ngOnInit(): void {
    const savedUserData = localStorage.getItem('userData');

    if (savedUserData) {
      const userData = JSON.parse(savedUserData);
      this.userId = userData.userId;
      console.log('User ID from localStorage:', this.userId);  // Log to check if userId is set
    }
  
    this.userStore.userId$.subscribe(userId => {
      this.userId = userId || '';
      console.log('User ID from userStore:', this.userId);  // Log to check if userId is set
    });
  
    // Ensure that userId is set before making the API call
    if (!this.userId) {
      console.error('User ID is not available');
      return;  // Stop further execution if userId is not available
    }

    if (this.activeDay === undefined) {
      const today = new Date();
      this.activeDay = today.getDate();
      console.log('Defaulting activeDay to today:', this.activeDay);
    }

    this.getEvents();
    this.initCalendar();
    this.userStore.userId$.subscribe(userId => {
      this.userId = userId || '';
      console.log('User ID:', this.userId);
    });
  }

  initCalendar() {
    this.days = [];
    const firstDay = new Date(this.year, this.month, 1);
    const lastDay = new Date(this.year, this.month + 1, 0);
    const prevLastDay = new Date(this.year, this.month, 0);
    const dayOfWeek = firstDay.getDay();
    const totalDays = lastDay.getDate();
    const prevDays = prevLastDay.getDate();
    const nextDays = 7 - lastDay.getDay() - 1;
  
    // Previous month's days
    for (let i = dayOfWeek; i > 0; i--) {
      this.days.push({
        date: prevDays - i + 1,
        isToday: false,
        isActive: false,
        isPrev: true,
        isNext: false,
        hasEvent: false,
      });
    }
  
    // Current month's days
    for (let i = 1; i <= totalDays; i++) {
      const today = new Date();
      const isToday =
        i === today.getDate() &&
        this.month === today.getMonth() &&
        this.year === today.getFullYear();
  
      const dateStr = `${this.year}-${String(this.month + 1).padStart(2, '0')}-${String(i).padStart(2, '0')}`;
      const hasEvent = this.eventsArr.some((e) => e.eventDate === dateStr);
  
      // Mark the active day
      const isActive = this.activeDay === i;
  
      this.days.push({
        date: i,
        isToday,
        isActive, // Mark the clicked day as active
        isPrev: false,
        isNext: false,
        hasEvent,
      });
    }
  
    // Next month's days
    for (let i = 1; i <= nextDays; i++) {
      this.days.push({
        date: i,
        isToday: false,
        isActive: false,
        isPrev: false,
        isNext: true,
        hasEvent: false,
      });
    }
  }
  
  
  prevMonth(): void {
    this.month--;
    if (this.month < 0) {
      this.month = 11;
      this.year--;
    }
    this.initCalendar();
  }

  nextMonth(): void {
    this.month++;
    if (this.month > 11) {
      this.month = 0;
      this.year++;
    }
    this.initCalendar();
  }

  getEvents(): void {
    if (this.activeDay === undefined) {

      console.error('Active day is not defined');
      return
    }

    // Construct the date string in 'yyyy-mm-dd' format
    const dateStr = `${this.year}-${String(this.month + 1).padStart(2, '0')}-${String(this.activeDay).padStart(2, '0')}`;

    // Make sure the date is correct and userId is set
    console.log('Fetching events for:', this.userId, dateStr);

    this.eventService.getEventDetails(this.userId, dateStr).subscribe({
      next: (data) => {
        this.eventsArr = data;  // Update the events array
        this.initCalendar();     // Reinitialize the calendar after fetching events
      },
      error: (err) => {
        console.error('Error fetching events:', err);
        alert('Error fetching events. Please check the console for more details.');
      }
    });
  }

  onDayClick(day: Day) {
    // If it's a previous or next month, navigate accordingly
    if (day.isPrev) {
      this.prevMonth();
      return;
    } else if (day.isNext) {
      this.nextMonth();
      return;
    }
  
    // Deactivate all days first
    this.days.forEach((d) => (d.isActive = false));
  
    // Activate the clicked day
    day.isActive = true;
  
    // Set the active day to the clicked day's date
    this.activeDay = day.date;
    console.log('Active Day:', this.activeDay)
  
    // Fetch the events for the selected day
    this.getEvents()
    this.getActiveDay(day.date)
    this.updateEvents(day.date)
  }

  updateEvents(day: number): void {
    const dateStr = `${this.year}-${String(this.month + 1).padStart(2, '0')}-${String(this.activeDay).padStart(2, '0')}`;
    this.dayEvents = this.eventsArr.filter(e => e.eventDate === dateStr);
  
    console.log('Updated events for date:', dateStr, this.dayEvents); // Log for debugging
  }

  deleteEvent(evToDelete: CalendarEvent): void {
    if (!evToDelete.eventId) return;
    this.eventService.deleteEvent(evToDelete.eventId, this.userId).subscribe({
      next: () => {
        this.getEvents(); // Refresh events list
      },
      error: (err) => console.error('Error deleting event:', err)
    });
  }

  saveEvents(): void {
    localStorage.setItem('events', JSON.stringify(this.eventsArr));
  }

  toggleAddEvent() {
    this.addEventOpen = true;
  }

  closeAddEvent() {
    this.addEventOpen = false;
  }

  formatTime(field: 'from' | 'to'): void {
    let value = field === 'from' ? this.newEventTimeFrom : this.newEventTimeTo;

    value = value.replace(/[^0-9:]/g, '');
    if (value.length === 2 && !value.includes(':')) {
      value += ':';
    }
    if (value.length > 5) {
      value = value.slice(0, 5);
    }

    if (field === 'from') {
      this.newEventTimeFrom = value;
    } else {
      this.newEventTimeTo = value;
    }
  }

  addEvent(): void {
    const title = this.newEventTitle.trim();
    const from = this.newEventTimeFrom.trim();
    const to = this.newEventTimeTo.trim();

    if (!title || !from || !to) {
      alert('Please fill all the fields');
      return;
    }

    const timeRegex = /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/;
    if (!timeRegex.test(from) || !timeRegex.test(to)) {
      alert('Invalid time format. Use HH:mm');
      return;
    }

    const formattedFrom = this.convertTime(from);
    const formattedTo = this.convertTime(to);
    const time = `${formattedFrom} - ${formattedTo}`;

    const eventDate = `${this.year}-${String(this.month + 1).padStart(2, '0')}-${String(this.activeDay).padStart(2, '0')}`;

    const event: CalendarEvent = {
      userId: this.userId,
      title,
      timeFrom: from,
      timeTo: to,
      eventDate
    };

    this.eventService.createEvent(event).subscribe({
      next: (response) => {
        const eventId = response.eventId;
        console.log("Created event with ID:", eventId);
        this.getEvents();
        this.newEventTitle = '';
        this.newEventTimeFrom = '';
        this.newEventTimeTo = '';
        this.closeAddEvent();
      },
      error: (err) => {
        console.error('Error saving event:', err);
        alert('Failed to save event.');
      }
    });
  }

  convertTime(time: string): string {
    const [hour, min] = time.split(':').map(Number);
    const suffix = hour >= 12 ? 'PM' : 'AM';
    const formattedHour = hour % 12 || 12;
    return `${formattedHour}:${min.toString().padStart(2, '0')} ${suffix}`;
  }

  getActiveDay(day: number): void {
    const date = new Date(this.year, this.month, day);
    const dayName = date.toLocaleDateString('en-US', { weekday: 'short' }); // e.g. "Wed"
    const daySuffix = this.getDaySuffix(day);
    const fullDate = `${day}${daySuffix} ${this.months[this.month]} ${this.year}`;
  
    this.selectedDayName = dayName;
    this.selectedDateText = fullDate;
  }

  getDaySuffix(day: number): string {
    if (day > 3 && day < 21) return 'th';
    switch (day % 10) {
      case 1: return 'st';
      case 2: return 'nd';
      case 3: return 'rd';
      default: return 'th';
    }
  }

  onDateInput(event: Event): void {
    const input = (event.target as HTMLInputElement).value;
    this.gotoDateInput = input
      .replace(/[^0-9/]/g, '')
      .slice(0, 7)
      .replace(/^(\d{2})(?!\/)/, '$1/')
  }


  gotoDate(): void {
    const [monthStr, yearStr] = this.gotoDateInput.split('/')
    const month = parseInt(monthStr, 10)
    const year = parseInt(yearStr, 10)

    if (!isNaN(month) && !isNaN(year) && month >= 1 && month <= 12 && yearStr.length === 4) {
      this.month = month - 1
      this.year = year
      this.initCalendar()
    } else {
      alert('Invalid Date')
    }
  }

  goToToday(): void {
    const today = new Date()
    this.month = today.getMonth()
    this.year = today.getFullYear()
    this.initCalendar()
  }
}
