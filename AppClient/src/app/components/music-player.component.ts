import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { SpotifyService } from '../services/SpotifyService';
import { CurrentSong, PlayBackState } from '../model/model';
import { map, Subscription } from 'rxjs';
import { WebSocketService } from '../services/WebSocketService';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-music-player',
  standalone: false,
  templateUrl: './music-player.component.html',
  styleUrl: './music-player.component.css'
})
export class MusicPlayerComponent implements OnInit, OnDestroy {

  private spotifySvc = inject(SpotifyService)
  private websocketSvc = inject(WebSocketService)
  private spotifyAccessToken: string = ''
  protected currentSong !: CurrentSong
  protected playBackState !: PlayBackState
  currentTime: number = 0;
  isPlaying: boolean = false;
  interval: any;
  duration: number = 0
  contextUri: string = '' 
  position: number = 0

  private subscription: Subscription = new Subscription()

  constructor(private cdRef: ChangeDetectorRef) {}


  ngOnInit(): void {
    this.spotifySvc.getSpotifyToken().subscribe({
      next: (token: string) => {
        if (token) {
          console.log("Retrieved token:", token)
          this.spotifyAccessToken = token
          this.websocketSvc.connect()
          this.subscription = this.websocketSvc.playbackState$.subscribe((playstate: PlayBackState | null) => {
            if (playstate) {
              console.log('Received playstate:', playstate);
              this.updateUI(playstate);
            }
          })
        }
      },
      error: (err) => {
        console.error("Error retrieving token:", err)
      }
    })
  }


  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.websocketSvc.disconnect();
  
  }

  updateUI(playstate: PlayBackState): void {
    this.currentTime = playstate.progress / 1000
    this.isPlaying = playstate.isPlaying
    this.currentSong = {
      songId: playstate.songId,
      songName: playstate.songName,
      artistName: playstate.artistName,
      imageUrl: playstate.albumImageUrl,
      duration: playstate.duration,
      progress: playstate.progress
    };

    console.log(this.currentSong)

    console.log('Updated current time:', this.currentTime)
    console.log('Is song playing?', this.isPlaying)
    this.cdRef.detectChanges()
  }

  // Spotify player
  spotifyLogin(): void {

    // Spotify authorization URL
    const spotifyAuthUrl = 'https://accounts.spotify.com/authorize?' +
      'response_type=code' +
      '&client_id=708779bb920c4b239632015c9e2e4c0d' +
      '&scope=streaming%20user-read-email%20user-read-private%20user-read-playback-state%20user-read-currently-playing%20user-modify-playback-state' +
      '&redirect_uri=http://localhost:8080/auth/callback'


    window.location.href = spotifyAuthUrl
  }

  logoutSpotify() {
    this.spotifyAccessToken = ''
    window.location.href = 'https://accounts.spotify.com/logout';
  }

  // getCurrentSong() {
  //   this.spotifySvc.getCurrentPlayingsong().subscribe({
  //     next: (songData: any) => {
  //       this.updateCurrentSong(songData);
  //     },
  //     error: (err) => {
  //       console.error("Error fetching current song:", err);
  //     }
  //   });
  // }

  // updateCurrentSong(songData: any) {
  //   if (songData) {
   
  //     this.currentSong = {
  //       songId: songData.songId,
  //       imageUrl: songData.imageUrl,  
  //       songName: songData.songTitle,  
  //       artistName: songData.artistName,  
  //       duration: songData.songDuration
  //     };
  
  //     // Reset current time
  //     this.currentTime = 0;
  
  //     console.log("Current song updated:", this.currentSong);
  //   }
  // }

  togglePlayPause(): void {
    const trackUris = [`spotify:track:${this.currentSong.songId}`]
    const offset = { position: 0 }
    const positionMs = this.currentTime * 1000;
    
    
    this.websocketSvc.sendMessage('toggle_play_pause', {
      uris: trackUris,
      offset: offset,
      position_ms: positionMs
    })
  
  }

  nextTrack(): void {
  
    this.websocketSvc.sendMessage('next', {})
    console.log('Sending next track command')
  }
  
  previousTrack(): void {
  
    this.websocketSvc.sendMessage('previous', {})
    console.log('Sending previous track command')
  }
  

  formatDuration(durationMs: number): string {
    if (durationMs == null || isNaN(durationMs)) {
      return '00:00'
    }

    const minutes = Math.floor(durationMs / 60000)
    const seconds = Math.floor((durationMs % 60000) / 1000)

    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`
  }

  getFormattedDuration(): string {
    const duration = this.currentSong?.duration || 0
    return this.formatDuration(duration)
  }

  updateCurrentTime(event: any): void {
    this.currentTime = event.target.value;
    console.log('Current Time:', this.currentTime);
}
  

}
