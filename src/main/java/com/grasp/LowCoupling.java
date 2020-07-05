package com.grasp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Если объекты в приложении сильно связанны, то любой их изменение приводит к изменениям во всех связанных объектах.
 * А это неудобно и порождает множество проблем.
 * Low coupling как раз говорит о том что необходимо, чтобы код был слабо связан и зависел только от абстракций.
 * Аналог DI (SOLID)
 */
public class LowCoupling {
    public static void main(String[] args) {
        Traveler traveler = new Traveler(new Bike());
        traveler.startJourney();
        traveler.setVehicle(new Car());
        traveler.startJourney();
    }

    private interface Vehicle {
        void move();
    }

    private static class Car implements Vehicle {
        @Override
        public void move() {
            System.out.println("Move car");
        }
    }

    private static class Bike implements Vehicle {
        @Override
        public void move() {
            System.out.println("Move bike");
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Traveler {
        private Vehicle vehicle;

        void startJourney() {
            vehicle.move();
        }
    }

}
