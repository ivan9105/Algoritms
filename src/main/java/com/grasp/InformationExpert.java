package com.grasp;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InformationExpert {
    /**
     * Шаблон определяет базовый принцип распределения ответственности
     *
     * Ответственность должна быть назначена тому, кто владеет максимумом необходимой информации для исполнения — информационному эксперту.
     */
    public static void main(String[] args) {
        ShoppingCard card = new ShoppingCard();
        card.addItem(OrderItem.builder()
                .description("Xiaomi Note 9 Pro")
                .price(new BigDecimal(16350))
                .quantity(1)
                .build()
        );

        card.totalSum();
    }

    /**
     * Informational Expert
     */
    private static class ShoppingCard {
        private final List<OrderItem> items = new ArrayList<>();

        public ShoppingCard() {
        }

        public void addItem(OrderItem orderItem) {
            //Not implemented yet
        }

        public void removeItem(OrderItem orderItem) {
            //Not implemented yet
        }

        /**
         * метод информационного эксперта
         */
        public BigDecimal totalSum() {
            return items.stream()
                    .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))).
                    reduce(BigDecimal::add).orElse(new BigDecimal("0.00"));
        }
    }

    @Data
    @Builder
    private static class OrderItem {
        private BigDecimal price;
        private Integer quantity;
        private String description;
    }
}
