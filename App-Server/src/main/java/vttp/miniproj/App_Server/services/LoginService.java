package vttp.miniproj.App_Server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.miniproj.App_Server.Repository.UserRepository;
import vttp.miniproj.App_Server.models.UserData;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepo;


    public boolean login(String username, String password) {

        UserData user = userRepo.findByUsername(username);
        System.out.println(user.toString());

       if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
            if(user.getIsVerified() == 1)
         return true;

       } 

       return false;
    } 
    
    public UserData getUserData(String username) {
        return userRepo.findByUsername(username);
    }
}
