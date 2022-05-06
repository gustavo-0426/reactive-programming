package com.co.softworld.reactive.programming.section2;

import com.co.softworld.reactive.programming.section2.flux.FluxApp;

public class Section2App {
    
    public static void main(String[] args) throws InterruptedException {
        FluxApp fluxApp = new FluxApp();
        fluxApp.fluxString();
        fluxApp.fluxException();
        fluxApp.fluxOnCompleted();
        fluxApp.fluxMap();
        fluxApp.fluxFilter();
        fluxApp.fluxObject();
        fluxApp.fluxImmutable();
        fluxApp.fluxFromList();
        fluxApp.fluxFlatMap();
        fluxApp.fluxFromListObjectToString();
        fluxApp.fluxToMono();
        fluxApp.fluxWithTwoMono();
        fluxApp.fluxWithTwoFlux();
        fluxApp.zipWith();
        fluxApp.zipWithFormTuple();
        fluxApp.fluxRange();
        fluxApp.fluxInterval();
        fluxApp.fluxDelayElement();
        fluxApp.fluxDelayElementInfinitive();
        fluxApp.fluxCreate();
        fluxApp.backPressure();
        fluxApp.backPressure2();
    }
}
