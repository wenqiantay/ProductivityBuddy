package vttp.miniproj.App_Server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisHttpSession
public class RedisConfig extends AbstractHttpSessionApplicationInitializer {

    @Value("${spring.redis.password}")
    private String redisPassword;
    

     @Bean
    public RedisConnectionFactory connectionFactory() {
        // Use RedisStandaloneConfiguration to configure connection properties
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        
        // Set the host and port of your Redis server
        redisConfig.setHostName("tramway.proxy.rlwy.net");
        redisConfig.setPort(29685);
        
        // Set the password for your Redis server (if any)
        redisConfig.setPassword(redisPassword);  // Replace with actual password

        // Create a LettuceConnectionFactory with the RedisStandaloneConfiguration
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig);

        // Return the connection factory
        return factory;
    }
}
