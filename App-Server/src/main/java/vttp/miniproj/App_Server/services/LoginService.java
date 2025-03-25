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

        if (user == null) {
            return false;
        }

        if(!password.equals(user.getPassword())) {
            return false;
        }

        if (!user.isVerified()) {
            return false; 
        }

        return true;

    }   
    
}
