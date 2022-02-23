package com.co.softworld.reactive.programming.flux;

import com.co.softworld.reactive.programming.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class FluxApp {

    private static final Logger log = LoggerFactory.getLogger(FluxApp.class);

    public static void fluxString() {

        Flux<String> name = Flux.just("Gustavo", "Martin", "Maye")
                .doOnNext(log::info);

        name.subscribe();
    }

    public static void fluxException() {

        Flux<String> names = Flux.just("Gustavo", "Martin", "", "Maye")
                .doOnNext(name -> {
                    if (name.isEmpty())
                        throw new RuntimeException("name is empty");
                    System.out.println(name);
                });

        names.subscribe(log::debug,
                error -> {
                    log.error(error.getMessage());
                });
    }

    public static void fluxOnCompleted() {

        Flux<String> names = Flux.just("Gustavo", "Martin", "Maye")
                .doOnNext(name -> {
                    if (name.isEmpty())
                        throw new RuntimeException("name is empty");
                    System.out.println(name);
                });

        names.subscribe(log::debug,
                error -> {
                    log.error(error.getMessage());
                },
                () -> log.info("completed"));
    }

    public static void fluxMap() {

        Flux<String> names = Flux.just("Gustavo", "Martin", "Maye")
                .doOnNext(System.out::println)
                .map(va -> va.toUpperCase());

        names.subscribe(log::info);
    }

    public static void fluxFilter() {

        Flux<String> names = Flux.just("Gustavo", "Martin", "Maye")
                .doOnNext(System.out::println)
                .map(name -> name.toUpperCase())
                .filter(name -> name.startsWith("MA"));

        names.subscribe(log::info);
    }

    public static void fluxObject() {

        AtomicInteger n = new AtomicInteger(0);
        Flux<User> user = Flux.just("Gustavo", "Martin", "Maye")
                .doOnNext((u) -> n.getAndIncrement())
                .map(name -> new User(n.get(), name, ""));

        user.subscribe(us -> {
            log.info(us.getId() + " ".concat(us.getName()).concat(" ").concat(us.getLastName()));
        });
    }

    /**
     * Los flux son de tipo inmutable, es decir no cambia su valor.
     * Por lo tanto cuando se utiliza numbers.filter, no cambia el valor de numbers
     */
    public static void fluxInmutable(){
        Flux<String> numbers = Flux.just("one", "two", "three");
        numbers.filter(number -> number.startsWith("t"));

        numbers.subscribe(log::info);
    }
}
