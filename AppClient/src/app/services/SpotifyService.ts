import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, lastValueFrom, Observable } from "rxjs";
import { CurrentSong, PlayBackState } from "../model/model";

@Injectable()
export class SpotifyService {

    private http = inject(HttpClient)

    getSpotifyToken(): Observable<string> {
        return this.http.get<string>('/api/spotify/token', { responseType: 'text' as 'json' })
    }

    getCurrentPlayingsong(): Observable<CurrentSong> {
      return this.http.get<CurrentSong>('/api/spotify/current-playing')
    }

    getCurrentPlayBackState(): Observable<PlayBackState> {
      return this.http.get<PlayBackState>('/api/spotify/playbackstate')
    }

  
}
