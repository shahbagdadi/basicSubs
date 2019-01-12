package com.netflix.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.netflix.subscription.core.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableScheduling
public class SubscriptionApplication
{

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionApplication.class, args);
	}
}
