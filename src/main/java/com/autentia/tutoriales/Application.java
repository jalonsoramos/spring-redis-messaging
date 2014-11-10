package com.autentia.tutoriales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.autentia.tutoriales.redis.StringRedisRepository;
import com.autentia.tutoriales.tweets.TweetIngestor;
import com.autentia.tutoriales.tweets.TweetsReceiver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(TweetIngestor.TWEETS_SUBSCRIPTION_CHANNEL));

		return container;
	}

	@Bean
	MessageListenerAdapter messageListenerAdapter(TweetsReceiver tweetsReceiver) {
		return new MessageListenerAdapter(tweetsReceiver, "receiveTweet");
	}

	@Bean
	TweetsReceiver receiver() {
		return new TweetsReceiver();
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	@Bean
	StringRedisRepository stringRedisRepository(StringRedisTemplate template) {
		return new StringRedisRepository(template);
	}
	
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}
}