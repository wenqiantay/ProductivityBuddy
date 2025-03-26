package vttp.miniproj.App_Server.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.miniproj.App_Server.models.UserData;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT="INSERT INTO user_data(userId, name, username, gender, email, birthdate, password, is_verified, verification_token) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CHECK_EMAIL="SELECT COUNT(*) FROM user_data WHERE email = ?";
    private static final String SQL_SELECT="SELECT * FROM user_data WHERE username = ?";
    private static final String SQL_CHECK_USERNAME="SELECT COUNT(*) FROM user_data WHERE username = ?";
    private static final String SQL_SELECT_BY_TOKEN = "SELECT * FROM user_data WHERE verification_token = ?";
    private static final String SQL_VERFIY_MANUAL="UPDATE user_data SET is_verified = 1 WHERE username = ?";

    // Inserting data to database
    public void insertUserData(UserData userData) {

        jdbcTemplate.update(
            SQL_INSERT, userData.getUserId(), userData.getName(), userData.getUsername(), 
            userData.getGender(), userData.getEmail(), userData.getBirthdate(),userData.getPassword(), userData.getIsVerified(),
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

    public UserData findByVerificationToken(String token) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_BY_TOKEN, new BeanPropertyRowMapper<>(UserData.class), token);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    public UserData findByUsername(String username) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT, username);
        while (rs.next()) {
           UserData user = new UserData();
        user.setUserId(rs.getString("userid"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setBirthdate(rs.getDate("birthdate"));
        user.setGender(rs.getString("gender"));
        user.setPassword(rs.getString("password"));
        user.setIsVerified(rs.getInt("is_verified"));
        return user;
       }

       return null;
    }

    public void verifyUserManually(String username) {
        jdbcTemplate.update(SQL_VERFIY_MANUAL, username);
    }

}
