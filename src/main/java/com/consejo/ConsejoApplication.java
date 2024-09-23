package com.consejo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.consejo.daos.EmailService;

@SpringBootApplication
public class ConsejoApplication {

	public static void main(String[] args) {
		System.setProperty("com.sun.net.ssl.checkRevocation", "false");
		SpringApplication.run(ConsejoApplication.class, args);
	}

}
