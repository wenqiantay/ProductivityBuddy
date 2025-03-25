package vttp.miniproj.App_Server.Controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vttp.miniproj.App_Server.models.Playstate;
import vttp.miniproj.App_Server.models.Song;
import vttp.miniproj.App_Server.services.SpotifyService;
import vttp.miniproj.App_Server.services.TokenService;

@RestController
public class SpotifyAuthController {

    @Autowired
    private SpotifyService spotifySvc;

    @Autowired
    private TokenService tokenSvc;

    @GetMapping("/auth/callback")
    public ResponseEntity<Void> spotifyCallback(String code, HttpSession session) {
      
        String accessToken = spotifySvc.exchangeCodeForToken(code);
        System.out.println(accessToken);

        // Storing accesstoken in Redis Session
        session.setAttribute("spotify_access_token", accessToken);
        tokenSvc.setAccessToken((String)session.getAttribute("spotify_access_token"));
        
        return ResponseEntity.status(302)
            .location(URI.create("http://localhost:4200/musicplayer"))
            .build();
    }

    @GetMapping("/api/spotify/token")
    public ResponseEntity<String> getSpotifyToken(HttpSession session) {

        String accessToken = (String) session.getAttribute("spotify_access_token");


        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/api/spotify/current-playing")
    public ResponseEntity<Song> getCurrentPlayingFromSpotify(HttpSession session){

        String accessToken = (String) session.getAttribute("spotify_access_token");

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Song currentSong = spotifySvc.getCurrentPlaying(accessToken);

        if (currentSong == null) {
            System.out.println("No song is currently playing.");
        } else {
            System.out.println("Current song: " + currentSong.toString());
        }


        return ResponseEntity.ok(currentSong);


    }

    @GetMapping("/api/spotify/playbackstate")
    public ResponseEntity<Playstate> getCurentPlayBackState(HttpSession session) {

        String accessToken = (String) session.getAttribute("spotify_access_token");

        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Playstate currentPlaystate = spotifySvc.getPlaystate(accessToken);

        return ResponseEntity.ok(currentPlaystate);

    }


   
}
