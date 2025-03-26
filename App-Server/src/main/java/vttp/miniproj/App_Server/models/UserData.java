package vttp.miniproj.App_Server.models;

import java.util.Date;

public class UserData {
    
    private String userId;
    private String name;
    private String username;
    private String gender;
    private String email;
    private Date birthdate;
    private String password;
    private String verificationToken;
    private int isVerified; 
    

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationToken() {
        return verificationToken;
    }
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
 
    public int getIsVerified() {
        return isVerified;
    }
    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }
   
    @Override
    public String toString() {
        return "UserData [userId=" + userId + ", name=" + name + ", username=" + username + ", gender=" + gender
                + ", email=" + email + ", birthdate=" + birthdate + ", password=" + password + ", verificationToken="
                + verificationToken + ", isVerified=" + isVerified + "]";
    }

    
      
}
