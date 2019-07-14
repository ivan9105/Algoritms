package com.patterns.generators.builder;

public class Car {
    private int seats;
    private Engine engine;
    private TripComputer tripComputer;
    private GPS gps;

    public static CarBuilder builder() {
        return new CarBuilder();
    }

    public static class CarBuilder {
        private Car instance = new Car();
        
        private CarBuilder() {
        }

        public CarBuilder seats(int seats) {
            instance.seats = seats;
            return this;
        }

        public CarBuilder engine(Engine engine) {
            instance.engine = engine;
            return this;
        }

        public CarBuilder tripComputer(TripComputer tripComputer) {
            instance.tripComputer = tripComputer;
            return this;
        }

        public CarBuilder gps(GPS gps) {
            instance.gps = gps;
            return this;
        }

        public Car build() {
            return instance;
        }
    }
}

class Engine {
}

class TripComputer {
}

class GPS {
}
