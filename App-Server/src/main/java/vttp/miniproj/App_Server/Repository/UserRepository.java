package vttp.miniproj.App_Server.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.miniproj.App_Server.models.UserData;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT="INSERT INTO user_data(userId, name, username, gender, email, birthdate, password, is_verified, verification_token) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CHECK_EMAIL="SELECT COUNT(*) FROM user_data WHERE email = ?";
    private static final String SQL_VERFIY_SELECT="SELECT COUNT(*) FROM user_data WHERE verification_token = ? AND is_verified = false";
    private static final String SQL_SELECT="SELECT * FROM user_data WHERE username = ?";
    private static final String SQL_CHECK_USERNAME="SELECT COUNT(*) FROM user_data WHERE username = ?";

    // Inserting data to database
    public void insertUserData(UserData userData) {

        jdbcTemplate.update(
            SQL_INSERT, userData.getUserId(), userData.getName(), userData.getUsername(), 
            userData.getGender(), userData.getEmail(), userData.getBirthdate(),userData.getPassword(), userData.isVerified(),
            userData.getVerificationToken());

    }

    // Check if email is unique
    public boolean checkEmailExist(String email){

        Integer count = jdbcTemplate.queryForObject(SQL_CHECK_EMAIL, Integer.class, email);
        return count != null && count > 0;
    }

    // Check if username is unique
    public boolean checkUsernameExist(String username) {
        
        Integer count = jdbcTemplate.queryForObject(SQL_CHECK_USERNAME, Integer.class, username);
        return count != null && count > 0;
    }


    public boolean verifyUserByToken(String token) {
    
        Integer count = jdbcTemplate.queryForObject(SQL_VERFIY_SELECT, Integer.class, token);
    
        if (count != null && count > 0) {
            String updateSql = "UPDATE user_data SET is_verified = true, verification_token = NULL WHERE verification_token = ?";
            jdbcTemplate.update(updateSql, token);
            return true;
        }
    
        return false;
    }

    public UserData findByUsername(String username) {

        try {
            return jdbcTemplate.queryForObject(SQL_SELECT, new BeanPropertyRowMapper<>(UserData.class), username);
        } catch (EmptyResultDataAccessException e) {
            return null; // No user found with the given username
        }
    }


}
