package vttp.miniproj.App_Server.Controller;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.miniproj.App_Server.models.UserData;
import vttp.miniproj.App_Server.services.RegistrationService;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationSvc;

    @PostMapping("/api/register")
    public ResponseEntity<Map<String, String>> postRegistration(@RequestBody String payload) throws ParseException{
        
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject userObj = reader.readObject();

        UserData userData = new UserData();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = userObj.getString("birthdate");
        Date birthDate = sdf.parse(dateStr);

        if (registrationSvc.checkEmailExist(userObj.getString("email"))) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Email is already registered"));
        }

        if(registrationSvc.checkUsernameExist(userObj.getString("username"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username already taken, Please choose a new username."));
        }

        userData.setUsername(userObj.getString("username"));
        userData.setName(userObj.getString("name"));
        userData.setEmail(userObj.getString("email"));
        userData.setGender(userObj.getString("gender"));
        userData.setBirthdate(birthDate);
        userData.setPassword(userObj.getString("password"));

        registrationSvc.insertUserData(userData);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User successfully registered");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
    UserData user = registrationSvc.findByVerificationToken(token);

    if (user != null) {
        String username = user.getUsername();
        registrationSvc.verifyUserManually(username);

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .header("Location", "http://tender-cat-production.up.railway.app/verified")  // Modify this URL
            .build();
    } else {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("status", "error", "message", "Invalid or expired token"));
        }
    }
    
}
