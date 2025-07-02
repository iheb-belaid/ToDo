package com.satage.ToDo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ToDoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoApplication.class, args);
	}

	@Bean
	CommandLineRunner run(TaskRepository repository) {
		return args -> {
			repository.save(new Task("Créer projet Spring Boot", false));
			repository.save(new Task("Configurer PostgreSQL", true));
		};
	}
}
