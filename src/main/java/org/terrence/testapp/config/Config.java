package org.terrence.testapp.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.terrence.testapp.Person;
import org.terrence.testapp.PersonRepository;

public class Config {

    @Configuration
    static class CloudConfiguration extends AbstractCloudConfig {

        @Bean
        public PersonRepository personRepository(RedisTemplate<String, Person> redisTemplate) {
            return new PersonRepository(redisTemplate);
        }

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            RedisConnectionFactory instance = connectionFactory().service(RedisConnectionFactory.class);
            return instance;
        }

        @Bean // this gets used under the covers somehow by the repo
        public RedisTemplate<String, Person> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<String, Person> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);

            RedisSerializer<String> stringSerializer = new StringRedisSerializer();
            RedisSerializer<Person> personSerializer = new Jackson2JsonRedisSerializer<>(Person.class);

            template.setKeySerializer(stringSerializer);
            template.setValueSerializer(personSerializer);
            template.setHashKeySerializer(stringSerializer);
            template.setHashValueSerializer(personSerializer);
            return template;
        }
    }
}