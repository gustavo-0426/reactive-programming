package com.co.softworld.reactive.programming.flux;

import com.co.softworld.reactive.programming.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FluxApp {

    private static final Logger log = LoggerFactory.getLogger(FluxApp.class);

    public static Flux<User> getFluxObject() {
        return Flux.just(
                new User(1, "Gustavo", "Castro"),
                new User(2, "Martin", "Castro"),
                new User(3, "Maye", "Sierra"));
    }
    
    public static Flux<String> getFlux() {
        return Flux.just("Gustavo", "Martin", "maye");
    }

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
    public static void fluxInmutable() {
        Flux<String> numbers = Flux.just("one", "two", "three");
        numbers.filter(number -> number.startsWith("t"));

        numbers.subscribe(log::info);
    }

    public static void fluxFromList() {
        List<String> letters = Arrays.asList("A", "B", "C", "D");
        Flux<String> flux = Flux.fromIterable(letters);

        Flux<String> result = flux
                .filter(let -> let.startsWith("A"));

        result.subscribe(log::info);

    }

    /**
     * El flatMap retorna un observable de tipo flux o mono solamente.
     */
    public static void fluxFlatMap() {
        Flux<String> flux = getFlux();
        Flux<String> result = flux.flatMap(name -> {
            if (name.toLowerCase().startsWith("ma")) {
                return Mono.just(name);
            } else {
                return Mono.empty();
            }
        });

        result.subscribe(log::info);
    }

    public static void fluxFromObjectToString() {
        List<User> names = Arrays.asList(
                new User(1, "Gustavo", "Castro"),
                new User(2, "Martin", "Castro"),
                new User(3, "Maye", "Sierra"));
        Flux<User> flux = Flux.fromIterable(names);

        Flux<String> result = flux
                .filter(user -> user.getLastName().equalsIgnoreCase("Castro"))
                .flatMap(user -> Mono.just(user))
                .map(user -> user.getName());

        result.subscribe(log::info);
    }

    public static void fluxToMono() {
        Mono<List<User>> flux = getFluxObject()
                .filter(user -> user.getLastName().equalsIgnoreCase("Castro"))
                .collectList();

        flux.subscribe(user -> log.info(user.toString()));
    }
}
