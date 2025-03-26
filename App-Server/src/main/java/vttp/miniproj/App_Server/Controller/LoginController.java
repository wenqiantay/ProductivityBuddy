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
import vttp.miniproj.App_Server.models.UserData;
import vttp.miniproj.App_Server.services.LoginService;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginSvc;

    @PostMapping("/api/login")
    public ResponseEntity<?> postLogin(@RequestBody String payload) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject loginObj = reader.readObject();

        String username = loginObj.getString("username");
        String password = loginObj.getString("password");
        System.out.println(username);
        System.out.println(password);

        boolean isLoggedIn = loginSvc.login(username, password);
        if (isLoggedIn) {
            UserData user = loginSvc.getUserData(username);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Json.createObjectBuilder()
                      .add("error", "Invalid username or password")
                      .build()
                      .toString());
        }
    }
}
