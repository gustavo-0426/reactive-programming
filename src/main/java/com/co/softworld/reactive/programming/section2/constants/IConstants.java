package com.co.softworld.reactive.programming.section2.constants;

import com.co.softworld.reactive.programming.section2.model.Comment;
import reactor.core.publisher.Flux;

public interface IConstants {
    String CASTRO = "Castro";
    String SIERRA = "Sierra";
    String CARDENAS = "Cardenas";
    String GUSTAVO = "Gustavo";
    String MAYE = "Maye";
    String MARTIN = "Martin";
    Flux<Integer> numbers = Flux.just(1, 2, 3, 4);
    Flux<String> lastName = Flux.just(CASTRO, SIERRA, CARDENAS, "");
    Flux<Comment> comment = Flux.just(
            new Comment("This is the first comment"),
            new Comment("This is the second comment"));
}
