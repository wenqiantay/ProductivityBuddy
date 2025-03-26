package vttp.miniproj.App_Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisHttpSession
public class RedisConfig extends AbstractHttpSessionApplicationInitializer {

     @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory("tramway.proxy.rlwy.net", 29685);
    }
}
