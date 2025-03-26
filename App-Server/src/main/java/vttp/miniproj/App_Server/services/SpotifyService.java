package vttp.miniproj.App_Server.services;

import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.miniproj.App_Server.models.Playstate;
import vttp.miniproj.App_Server.models.Song;


@Service
public class SpotifyService {
    
    @Value("${spotify.client.id}")
    private String spotifyClientId;

    @Value("${spotify.client.secret}")
    private String spotifyClientSecret;

    private final String REDIRECT_URI = "https://tender-cat-production.up.railway.app/auth/callback";
    private static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SPOTIFY_GET_CURRENT_PLAYING = "https://api.spotify.com/v1/me/player/currently-playing";
    private static final String SPOTIFY_GET_PLAYBACK_STATE="https://api.spotify.com/v1/me/player";
    private final String PLAYBACK_URL = "https://api.spotify.com/v1/me/player/play";
    private final String PAUSE_URL = "https://api.spotify.com/v1/me/player/pause";
    private final String NEXT_URL="https://api.spotify.com/v1/me/player/next";
    private final String PREV_URL="https://api.spotify.com/v1/me/player/previous";

    public String exchangeCodeForToken(String code) {
     
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("code", code);
        data.add("redirect_uri", REDIRECT_URI);
        data.add("grant_type", "authorization_code");
        data.add("client_id", spotifyClientId);
        data.add("client_secret", spotifyClientSecret);

    
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(data, headers);

        try {
          
            ResponseEntity<String> response = restTemplate.exchange(SPOTIFY_TOKEN_URL, HttpMethod.POST, entity, String.class);
            System.out.println(response.getBody());

            String accessToken = extractAccessTokenFromResponse(response.getBody());

            return accessToken;

        } catch (Exception e) {

            e.printStackTrace();
            
            return null;
        }
    }

    // Retrieving AccessToken from Spotify
    private String extractAccessTokenFromResponse(String responseBody) {
        
        JsonReader reader = Json.createReader(new StringReader(responseBody));
        JsonObject jsonResponse = reader.readObject();
        System.out.println(jsonResponse.getString("access_token"));

        return jsonResponse.getString("access_token");
    }

    public Song getCurrentPlaying(String token) {
        
        Song currentSong = new Song();
            
        // Set up the headers with the access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); 
        RequestEntity<Void> req = RequestEntity.get(URI.create(SPOTIFY_GET_CURRENT_PLAYING))
                                                .headers(headers)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .build();

        
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> response = template.exchange(req, String.class);

        try {

            JsonReader reader = Json.createReader(new StringReader(response.getBody()));
            JsonObject responseObj = reader.readObject();
            JsonObject itemObj = responseObj.getJsonObject("item");
            JsonObject albumObj = itemObj.getJsonObject("album");


            String id = albumObj.getString("id");
            currentSong.setSongId(id);

            JsonArray imageArray = albumObj.getJsonArray("images");
            for(int i = 0; i < imageArray.size(); i++) {
                JsonObject imageObj = imageArray.getJsonObject(0);
                String imageUrl = imageObj.getString("url");
                currentSong.setImageUrl(imageUrl);
            }

            String songTitle = itemObj.getString("name");
            currentSong.setSongTitle(songTitle);

            JsonArray artistArray = itemObj.getJsonArray("artists");
            List<String> artists = new ArrayList<>();
            for(int i = 0; i < artistArray.size(); i++) {
                JsonObject obj = artistArray.getJsonObject(i);
                String artist = obj.getString("name");
                artists.add(artist);
            }
            currentSong.setArtistName(artists);

            long songDurationMs = itemObj.getJsonNumber("duration_ms").longValue();
            currentSong.setSongDuration(songDurationMs);

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Current song: " + currentSong.toString());
        return currentSong;
        
    }

    public Playstate getPlaystate(String token){

        Playstate currentPlayBackState = new Playstate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); 

        RequestEntity<Void> req = RequestEntity.get(URI.create(SPOTIFY_GET_PLAYBACK_STATE))
                                                .headers(headers)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> response = template.exchange(req, String.class);

        try {
            
            JsonReader reader = Json.createReader(new StringReader(response.getBody()));
            JsonObject playbackObj = reader.readObject();
            JsonObject deviceObj = playbackObj.getJsonObject("device");

            String deviceId = deviceObj.getString("id");
            boolean isPlaying = playbackObj.getBoolean("is_playing");
            long progressMs = playbackObj.getJsonNumber("progress_ms").longValue();

            currentPlayBackState.setDeviceId(deviceId);
            currentPlayBackState.setPlaying(isPlaying);
            currentPlayBackState.setProgress(progressMs);

             
            JsonObject itemObj = playbackObj.getJsonObject("item");
            String songTitle = itemObj.getString("name");
            currentPlayBackState.setSongName(songTitle);

            String songId = itemObj.getString("id");  
            currentPlayBackState.setSongId(songId);

            JsonObject albumObj = itemObj.getJsonObject("album");
            String albumId = albumObj.getString("id");

            long songDurationMs = itemObj.getJsonNumber("duration_ms").longValue();
            currentPlayBackState.setDuration(songDurationMs);

            JsonArray imageArray = albumObj.getJsonArray("images");
            String imageUrl = imageArray.getJsonObject(0).getString("url");
            currentPlayBackState.setAlbumImageUrl(imageUrl);

            JsonArray artistArray = itemObj.getJsonArray("artists");
            List<String> artists = new ArrayList<>();
            for (int i = 0; i < artistArray.size(); i++) {
                JsonObject artistObj = artistArray.getJsonObject(i);
                artists.add(artistObj.getString("name"));
            }
            currentPlayBackState.setArtistName(artists);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentPlayBackState;
    }

   public String playTrack(String accessToken, String deviceId, List<String> trackUris, long positionMs) throws JsonProcessingException {
    String url = PLAYBACK_URL + "?device_id=" + deviceId;

    // Construct the request body with the "uris" field for multiple tracks
    String requestBody = String.format(
            "{\"uris\":%s, \"position_ms\":%d}",
            new ObjectMapper().writeValueAsString(trackUris), positionMs);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("Content-Type", "application/json");

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    // Make the PUT request to the Spotify API
    try {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        return response.getBody();
    } catch (HttpClientErrorException e) {
        System.err.println("Error: " + e.getResponseBodyAsString());
        return null;
    }
}

    public String pauseTrack(String accessToken, String deviceId) {
        String url = PAUSE_URL + "?device_id=" + deviceId;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
    
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        return response.getBody();
    }

    public String previousTrack(String token, String deviceId) {
        String url = PREV_URL + "?device_id=" + deviceId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    
    public String nextTrack(String token, String deviceId) {
        String url = NEXT_URL + "?device_id=" + deviceId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String refreshSpotifyAccessToken(String refreshToken) {
        
        String refreshUrl = "https://accounts.spotify.com/api/token";
        String requestBody = String.format("grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s",
                refreshToken, spotifyClientId, spotifyClientSecret);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(refreshUrl, HttpMethod.POST, entity, String.class);
    
        JsonReader reader = Json.createReader(new StringReader(response.getBody()));
        JsonObject jsonResponse = reader.readObject();
        String newAccessToken = jsonResponse.getString("access_token");
    
        return newAccessToken;
    }

}
