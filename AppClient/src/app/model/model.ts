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
