package com.co.softworld.reactive.programming;

import com.co.softworld.reactive.programming.flux.FluxApp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactivaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactivaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//FluxApp.fluxString();
		//FluxApp.fluxException();
		//FluxApp.fluxOnCompleted();
		//FluxApp.fluxMap();
		//FluxApp.fluxFilter();
		//FluxApp.fluxObject();
		FluxApp.fluxInmutable();
	}
}
