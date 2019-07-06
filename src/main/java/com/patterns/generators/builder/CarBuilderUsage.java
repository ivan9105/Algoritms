package com.patterns.generators.builder;

public class CarBuilderUsage {
    public static void main(String[] args) {
        Car.builder()
                .engine(new Engine())
                .gps(new GPS())
                .tripComputer(new TripComputer())
                .seats(4)
                .build();
    }
}
