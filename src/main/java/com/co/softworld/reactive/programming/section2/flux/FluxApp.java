package com.co.softworld.reactive.programming.section2.flux;

import com.co.softworld.reactive.programming.section2.model.Comment;
import com.co.softworld.reactive.programming.section2.model.User;
import com.co.softworld.reactive.programming.section2.model.UserComment;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class FluxApp {

    private static final Logger log = LoggerFactory.getLogger(FluxApp.class);

    public static Flux<Comment> getFluxComment() {
        return Flux.just(
                new Comment("This is the first comment"),
                new Comment("This is the second comment"));
    }

    public static Flux<User> getFluxUser() {
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
     * El flatMap retorna un observable de tipo flux o mono.
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
        Mono<List<User>> flux = getFluxUser()
                .filter(user -> user.getLastName().equalsIgnoreCase("Castro"))
                .collectList();

        flux.subscribe(user -> log.info(user.toString()));
    }

    public static User createUser(int id, String name, String lastName) {
        return new User(id, name, lastName);
    }

    public static Comment createComment(String description) {
        return new Comment(description);
    }

    public static void fluxWithTwoMono() {
        Mono<User> monoUser = Mono.fromCallable(() -> createUser(1, "Gustavo", "Castro"));
        Mono<Comment> monoComment = Mono.fromCallable(() -> createComment("First comment"));

        monoUser.flatMap(user -> monoComment.map(comment -> new UserComment(user, comment)))
                .subscribe(data -> log.info(data.toString()));
    }

    public static void fluxWithTwoFlux() {
        Flux<User> fluxUser = getFluxUser();
        Flux<Comment> fluxComment = getFluxComment();

        fluxUser.flatMap(user -> fluxComment.map(comment -> new UserComment(user, comment)))
                .subscribe(data -> log.info(data.toString()));
    }

    public static void zipWith() {
        Flux<User> fluxUser = getFluxUser();
        Flux<Comment> fluxComment = getFluxComment();

        fluxUser.zipWith(fluxComment, (user, commentary) -> new UserComment(user, commentary))
                .subscribe(data -> log.info(data.toString()));
    }

    public static void zipWithFormTuple() {
        Flux<User> fluxUser = getFluxUser();
        Flux<Comment> fluxComment = getFluxComment();

        fluxUser.zipWith(fluxComment)
                .map(tuple -> {
                    User user = tuple.getT1();
                    Comment comment = tuple.getT2();
                    return new UserComment(user, comment);
                })
                .subscribe(data -> log.info(data.toString()));
    }

    public static void fluxRange() {
        Flux<Integer> fluxRange = Flux.range(0, 5);
        Flux.just(2, 4, 6, 8)
                .map(number -> number * 3)
                .zipWith(fluxRange)
                .subscribe(data -> log.info(data.toString()));
    }

    public static void fluxRange2() {
        Flux<Integer> fluxRange = Flux.range(4, 4);
        Flux.just(1, 2, 3, 4, 5)
                .map(number -> number * 2)
                .zipWith(fluxRange)
                .subscribe(data -> log.info(data.toString()));
    }

    public static void fluxInterval() {
        Flux<Integer> fluxRange = Flux.range(1, 4);
        Flux<Long> fluxInterval = Flux.interval(Duration.ofSeconds(1));
        fluxRange
                .zipWith(fluxInterval, (range, interval) -> range)
                .doOnNext(range -> log.info(String.valueOf(range)))
                .blockLast();
    }

    public static void fluxDelayElement() {
        Flux<Integer> fluxRange = Flux.range(1, 4);
        fluxRange
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(range -> log.info(String.valueOf(range)))
                .blockLast();
    }

    public static void fluxDelayElementInfinitive() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(1);

        Flux.interval(Duration.ofSeconds(1))
                .doOnTerminate(count::countDown)
                .flatMap(data -> {
                    if (data == 8)
                        return Flux.error(new InterruptedException("must not be greater than 8"));
                    return Flux.just(data);
                })
                .map(data -> String.valueOf(data))
                .retry(1)
                .subscribe(log::info, error -> log.error(error.getMessage()));

        count.await();
    }

    public static void fluxCreate() {
        Flux.create(create -> {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        private int count = 1;

                        @Override
                        public void run() {
                            create.next(count++);
                            if (count == 4) {
                                timer.cancel();
                                create.complete();
                            }

                        }
                    }, 500, 1000);
                })
                .map(data -> String.valueOf(data))
                .subscribe(log::info,
                        error -> log.error(error.getMessage()),
                        () -> log.info(("completed")));
    }

    public static void backPressure() {
        Flux.range(1, 5)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;
                    private int limit = 2;
                    private int init = 0;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(limit);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info(String.valueOf(integer));
                        init++;
                        if (init == limit) {
                            init = 0;
                            subscription.request(limit);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void backPressure2() {
        Flux.range(1, 5)
                .log()
                .limitRate(2)
                .subscribe(data -> log.info(String.valueOf(data)));
    }

}
