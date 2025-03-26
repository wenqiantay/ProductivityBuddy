import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, lastValueFrom, map, Observable } from "rxjs";
import { CurrentSong, PlayBackState } from "../model/model";

@Injectable()
export class SpotifyService {

    private http = inject(HttpClient)

    getSpotifyToken(): Observable<string> {
        return this.http.get<string>('/api/spotify/token', { responseType: 'text' as 'json' })
    }

    getClientId(): Promise<string> {
      return firstValueFrom(this.http.get<any>('/api/client-id')).then(response => {
       
        return response.clientId.string
      });
    }

    getCurrentPlayingsong(): Observable<CurrentSong> {
      return this.http.get<CurrentSong>('/api/spotify/current-playing')
    }

    getCurrentPlayBackState(): Observable<PlayBackState> {
      return this.http.get<PlayBackState>('/api/spotify/playbackstate')
    }

  
}
