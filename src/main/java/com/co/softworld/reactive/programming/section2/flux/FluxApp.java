package com.co.softworld.reactive.programming.section2.flux;

import com.co.softworld.reactive.programming.section2.model.Comment;
import com.co.softworld.reactive.programming.section2.model.User;
import com.co.softworld.reactive.programming.section2.model.UserComment;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.co.softworld.reactive.programming.section2.constants.IConstants.*;

@Slf4j
public class FluxApp {

    List<User> userList = Arrays.asList(new User(1, GUSTAVO, CASTRO), new User(2, MARTIN, CASTRO), new User(3, MAYE, SIERRA));

    public void fluxString() {
        log.info("fluxString...");
        Flux.just(MARTIN, MAYE)
                .doOnNext(log::info)
                .subscribe();
    }

    public void fluxException() {
        log.info("fluxException...");
        Flux<String> flux = lastName
                .doOnNext(lastName -> {
                    if (lastName.isEmpty())
                        throw new RuntimeException("lastName is empty");
                });
        flux.subscribe(log::info,
                error -> log.error(error.getMessage()));
    }

    public void fluxOnCompleted() {
        log.info("fluxOnCompleted...");
        lastName.subscribe(log::info,
                error -> log.error(error.getMessage()),
                () -> log.info("completed"));
    }

    public void fluxMap() {
        log.info("fluxMap...");
        lastName.map(String::toUpperCase)
                .subscribe(log::info);
    }

    public void fluxFilter() {
        log.info("fluxFilter...");
        lastName.map(String::toUpperCase)
                .filter(data -> data.startsWith("S"))
                .subscribe(log::info);
    }

    public void fluxObject() {
        log.info("fluxObject...");
        AtomicInteger n = new AtomicInteger(0);
        lastName.doOnNext(u -> n.getAndIncrement())
                .map(data -> new User(n.get(), "", data))
                .subscribe(us -> log.info(us.getId() + " ".concat(us.getName()).concat(" ").concat(us.getLastName())));
    }

    /**
     * The flux are immutable, because its value does not change.
     */
    public void fluxImmutable() {
        log.info("fluxImmutable...");
        lastName.filter(number -> number.startsWith("C"));
        lastName.subscribe(log::info);
    }

    public void fluxFromList() {
        log.info("fluxFromList...");
        List<String> letters = Arrays.asList("A", "B", "C", "D");
        Flux.fromIterable(letters)
                .filter(let -> let.startsWith("A"))
                .subscribe(log::info);
    }

    /**
     * The flatMap return an observable of type flux or mono.
     */
    public void fluxFlatMap() {
        log.info("fluxFlatMap...");
        lastName.flatMap(lastName -> {
            if (lastName.toLowerCase().startsWith("s")) {
                return Mono.just(lastName);
            } else {
                return Mono.empty();
            }
        }).subscribe(log::info);
    }

    public void fluxFromListObjectToString() {
        log.info("fluxFromListObjectToString...");
        Flux.fromIterable(userList)
                .filter(use -> use.getLastName().equalsIgnoreCase(CASTRO))
                .flatMap(Mono::just)
                .map(User::getName)
                .subscribe(log::info);
    }

    public void fluxToMono() {
        log.info("fluxToMono...");
        Flux.fromIterable(userList)
                .filter(us -> us.getLastName().equalsIgnoreCase(CASTRO))
                .collectList()
                .subscribe(users -> log.info(users.toString()));
    }

    public void fluxWithTwoMono() {
        log.info("fluxWithTwoMono...");
        Mono<User> monoUser = Mono.fromCallable(() -> new User(1, GUSTAVO, CASTRO));
        Mono<Comment> monoComment = Mono.fromCallable(() -> new Comment("First comment"));

        monoUser.flatMap(us -> monoComment.map(comment -> new UserComment(us, comment)))
                .subscribe(data -> log.info(data.toString()));
    }

    public void fluxWithTwoFlux() {
        log.info("fluxWithTwoFlux...");
        Flux.fromIterable(userList)
                .flatMap(user -> comment.map(comment -> new UserComment(user, comment)))
                .subscribe(data -> log.info(data.toString()));
    }

    public void zipWith() {
        log.info("zipWith...");
        Flux.fromIterable(userList)
                .zipWith(comment, UserComment::new)
                .subscribe(data -> log.info(data.toString()));
    }

    public void zipWithFormTuple() {
        log.info("zipWithFormTuple...");
        Flux.fromIterable(userList)
                .zipWith(comment)
                .map(tuple -> {
                    User user = tuple.getT1();
                    Comment comment = tuple.getT2();
                    return new UserComment(user, comment);
                })
                .subscribe(data -> log.info(data.toString()));
    }

    public void fluxRange() {
        log.info("fluxRange...");
        Flux<Integer> fluxRange = Flux.range(0, 5);
        numbers.map(number -> number * 3)
                .zipWith(fluxRange)
                .subscribe(data -> log.info(data.toString()));
    }

    public void fluxInterval() {
        log.info("fluxInterval...");
        Flux<Long> fluxInterval = Flux.interval(Duration.ofMillis(300));
        Flux.range(1, 4)
                .zipWith(fluxInterval, (range, interval) -> range)
                .doOnNext(range -> log.info(String.valueOf(range)))
                .blockLast();
    }

    public void fluxDelayElement() {
        log.info("fluxDelayElement...");
        Flux.range(1, 4)
                .delayElements(Duration.ofMillis(300))
                .doOnNext(range -> log.info(String.valueOf(range)))
                .blockLast();
    }

    public void fluxDelayElementInfinitive() throws InterruptedException {
        log.info("fluxDelayElementInfinitive...");
        CountDownLatch count = new CountDownLatch(1);
        Flux.interval(Duration.ofMillis(300))
                .doOnTerminate(count::countDown)
                .flatMap(data -> {
                    if (data == 5) {
                        return Flux.error(new InterruptedException("must not be greater than 8"));
                    }
                    return Flux.just(data);
                })
                .map(String::valueOf)
                .retry(1)
                .subscribe(log::info, error -> log.error(error.getMessage()));
        count.await();
    }

    public void fluxCreate() {
        log.info("fluxCreate...");
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
                .map(String::valueOf)
                .subscribe(log::info,
                        error -> log.error(error.getMessage()),
                        () -> log.info(("completed")));
    }

    public void backPressure() {
        log.info("backPressure...");
        Flux.range(1, 5)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;
                    private static final int LIMIT = 2;
                    private int init = 0;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(LIMIT);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info(String.valueOf(integer));
                        init++;
                        if (init == LIMIT) {
                            init = 0;
                            subscription.request(LIMIT);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.error(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });
    }

    public void backPressure2() {
        log.info("backPressure2...");
        Flux.range(1, 5)
                .log()
                .limitRate(2)
                .subscribe(data -> log.info(String.valueOf(data)));
    }

}
