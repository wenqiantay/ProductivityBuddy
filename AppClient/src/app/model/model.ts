export interface GoogleClient {
    googleClientId : string
}

export interface UserData {

    name: string
    username: string
    email: string
    birthdate: string
    gender: string
    password: string

}

export interface UserDetail {
  userId: string
  username: string
  email: string
  birthdate: string 
  gender: string
  verified: number
}

export interface CurrentSong {
    songId: string
    imageUrl: string
    songName: string
    artistName: string[]
    duration: number
    progress: number
}

export interface PlayBackState {
    deviceId: string        
    progress: number         
    songName: string          
    albumImageUrl: string     
    artistName: string[]      
    isPlaying: boolean     
    duration: number
    songId: string          
}

export interface Login {
    username: string
    password: string
}


//Calendar
export interface CalendarEvent {
    eventId?: string
    userId: string
    title: string
    timeFrom: string
    timeTo: string
    eventDate: string //ISO format : 'YYYY-MM-DD'
}

export interface Day {
    date: number;
    isToday: boolean;
    isActive: boolean;
    isPrev: boolean;
    isNext: boolean;
    hasEvent: boolean;
}

export interface EventItem {
    id: string;
  title: string;
  time: string;
}