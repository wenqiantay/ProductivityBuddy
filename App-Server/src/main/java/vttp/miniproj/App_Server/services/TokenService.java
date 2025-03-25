package vttp.miniproj.App_Server.services;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private String accessToken;

   
    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
    
}
