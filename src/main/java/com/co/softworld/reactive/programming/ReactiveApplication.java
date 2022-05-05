package com.co.softworld.reactive.programming;

import com.co.softworld.reactive.programming.section2.flux.FluxApp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//FluxApp.fluxString();
		//FluxApp.fluxException();
		//FluxApp.fluxOnCompleted();
		//FluxApp.fluxMap();
		//FluxApp.fluxFilter();
		//FluxApp.fluxObject();
		//FluxApp.fluxInmutable();
		//FluxApp.fluxFromList();
		//FluxApp.fluxFlatMap();
		//FluxApp.fluxFromObjectToString();
		//FluxApp.fluxToMono();
		//FluxApp.fluxWithTwoMono();
		//FluxApp.fluxWithTwoFlux();
		//FluxApp.zipWith();
		//FluxApp.zipWithFormTuple();
		//FluxApp.fluxRange();
		//FluxApp.fluxRange2();
		//FluxApp.fluxInterval();
		//FluxApp.fluxDelayElement();
		//FluxApp.fluxDelayElementInfinitive();
		FluxApp.fluxCreate();
	}
}
