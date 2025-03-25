package vttp.miniproj.App_Server.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import vttp.miniproj.App_Server.models.MessageData;
import vttp.miniproj.App_Server.models.Playstate;
import vttp.miniproj.App_Server.services.SpotifyService;
import vttp.miniproj.App_Server.services.TokenService;

@Controller
public class WebSocketController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private TokenService tokenSvc;

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // polling every 5 seconds
    @Scheduled(fixedRate = 5000) 
    public void sendPlayStateUpdate() {
        
        String token = tokenSvc.getAccessToken();

        if (token != null) {
            
            Playstate playstate = spotifyService.getPlaystate(token);

            if (playstate != null) {
              
                messagingTemplate.convertAndSend("/topic/song", playstate);
            } else {
                System.out.println("Error: Failed to retrieve playback state.");
            }
        } else {
            System.out.println("No access token found.");
        }
    }

    @MessageMapping("/spotify") // Listen for messages from WebSocket
    public void handlePlayPauseMessage(MessageData messageData) throws JsonProcessingException {
        String token = tokenSvc.getAccessToken(); 

        if ("toggle_play_pause".equals(messageData.getCommand())) {
            // Get the current playback state
            Playstate playstate = spotifyService.getPlaystate(token);

            // Extract necessary details
            boolean isPlaying = playstate.isPlaying();
            String deviceId = playstate.getDeviceId();
            long positionMs = playstate.getProgress();
            List<String> trackUris = messageData.getData().getUris(); 

            if (isPlaying) {

                // Pause the track
                spotifyService.pauseTrack(token, deviceId);
            } else {

                // Play the track
                spotifyService.playTrack(token, deviceId, trackUris, positionMs);
            }

            messagingTemplate.convertAndSend("/topic/song", playstate);
        }

        if ("previous".equals(messageData.getCommand())) {
          
            Playstate playstate = spotifyService.getPlaystate(token);
            String deviceId = playstate.getDeviceId();
            spotifyService.previousTrack(token, deviceId); 
    
            
            playstate = spotifyService.getPlaystate(token);
            messagingTemplate.convertAndSend("/topic/song", playstate);
        }
    
        if ("next".equals(messageData.getCommand())) {
           
            Playstate playstate = spotifyService.getPlaystate(token);
            String deviceId = playstate.getDeviceId();
            spotifyService.nextTrack(token, deviceId); 
    
            
            playstate = spotifyService.getPlaystate(token);
            messagingTemplate.convertAndSend("/topic/song", playstate);
        }

    }
}
