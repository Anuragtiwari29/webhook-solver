package com.apoorv.webhookapp;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebhookappApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebhookappApplication.class, args);
	}

	@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}


}
