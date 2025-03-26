import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { SpotifyService } from '../services/SpotifyService';
import { CurrentSong, PlayBackState } from '../model/model';
import { map, Subscription } from 'rxjs';
import { WebSocketService } from '../services/WebSocketService';
import { ChangeDetectorRef } from '@angular/core';
import { UserStore } from '../user.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-music-player',
  standalone: false,
  templateUrl: './music-player.component.html',
  styleUrl: './music-player.component.css'
})
export class MusicPlayerComponent implements OnInit, OnDestroy {

  private userStore = inject(UserStore)
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

  private router = inject(Router)

  clientId: string = ''

  private subscription: Subscription = new Subscription()

  constructor(private cdRef: ChangeDetectorRef) { }


  ngOnInit(): void {

    this.getClientId()

    this.spotifySvc.getSpotifyToken().subscribe({
      next: (token: string) => {
        if (token) {
          this.spotifyAccessToken = token
          this.userStore.setSpotifyToken(token);
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
      this.subscription.unsubscribe()
    }
    this.websocketSvc.disconnect()

  }

  getClientId() {
    this.spotifySvc.getClientId().then(clientId => {
      this.clientId = clientId
    }).catch(err => {
      console.error('Error fetching client ID:', err)
    })
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

  spotifyLogin(): void {
  
    const spotifyAuthUrl = 'https://accounts.spotify.com/authorize?' +
      'response_type=code' +
      `&client_id=${this.clientId}` +
      '&scope=streaming%20user-read-email%20user-read-private%20user-read-playback-state%20user-read-currently-playing%20user-modify-playback-state' +
      '&redirect_uri=https://tender-cat-production.up.railway.app/auth/callback'; 

    // Open the Spotify login page in a new popup window
    const width = 600
    const height = 600
    const left = (window.innerWidth / 2) - (width / 2)
    const top = (window.innerHeight / 2) - (height / 2)
    const popup = window.open(spotifyAuthUrl, 'Spotify Login', `width=${width},height=${height},top=${top},left=${left}`)

    if (popup === null) {
      console.error("Popup window could not be opened. Please check your browser settings.")
      return
    }

   
    const messageListener = (event: MessageEvent) => {
      if (event.origin === window.location.origin && event.data.type === 'spotify-login') {
        const { accessToken } = event.data
        this.userStore.setSpotifyToken(accessToken)
  
      
        popup.close();
        this.router.navigate(['/dashboard'])
      }
    }

    window.addEventListener('message', messageListener);
  }

  logoutSpotify() {
    this.spotifyAccessToken = ''
    window.location.href = 'https://accounts.spotify.com/logout';
  }

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
