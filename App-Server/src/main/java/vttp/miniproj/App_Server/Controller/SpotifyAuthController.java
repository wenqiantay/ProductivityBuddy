package vttp.miniproj.App_Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
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

    @Value("${spotify.client.id}")
    private String spotifyClientId;

    @GetMapping("/api/client-id")
    public ResponseEntity<?> getClientId() {
        String clientId = spotifyClientId;
        JsonObject response = Json.createObjectBuilder()
                .add("clientId", clientId)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<String> spotifyCallback(String code, HttpSession session) {

        String accessToken = spotifySvc.exchangeCodeForToken(code);
        System.out.println(accessToken);

        // Storing accesstoken in Redis Session
        session.setAttribute("spotify_access_token", accessToken);
        tokenSvc.setAccessToken((String) session.getAttribute("spotify_access_token"));

        // Return HTML response for the popup page
        String callbackPage = "<!DOCTYPE html>"
                + "<html><head><title>Spotify Login Callback</title></head><body>"
                + "<script>"
                + "const accessToken = '" + accessToken + "';"
                + "window.opener.postMessage({type: 'spotify-login', accessToken: accessToken}, window.location.origin);"
                + "window.close();"
                + "</script>"
                + "</body></html>";

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(callbackPage);
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
    public ResponseEntity<Song> getCurrentPlayingFromSpotify(HttpSession session) {

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
