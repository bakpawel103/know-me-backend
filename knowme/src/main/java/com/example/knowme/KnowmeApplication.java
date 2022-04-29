package com.example.knowme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KnowmeApplication {

	@RequestMapping("/")
    @ResponseBody
    String home() {
      return "Hello World!";
    }
	
	public static void main(String[] args) {
		SpringApplication.run(KnowmeApplication.class, args);
	}
}
