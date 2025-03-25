package vttp.miniproj.App_Server.Controller;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.miniproj.App_Server.services.LoginService;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginSvc;

    @PostMapping("/api/login")
    public ResponseEntity<String> postLogin(@RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject loginObj = reader.readObject();

        String username = loginObj.getString("username");
        String password = loginObj.getString("password");

        boolean isLoggedIn = loginSvc.login(username, password);
        if (isLoggedIn) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or unverified user");
        }
    }
}
