package com.grasp;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Creator {
    /**
     * Creator или Создатель — суть ответственности такого объекта в том, что он создает другие объекты.
     * Сразу напрашивается аналогия с абстрактной фабрикой.
     *
     * Создатель содержит или агрегирует создаваемые объекты;
     * Создатель использует создаваемые объекты ;
     * Создатель знает, как проинициализировать создаваемый объект ;
     * Создатель записывает создаваемые объекты
     * Создатель имеет данные инициализации для A
     */
    public static void main(String[] args) {
        ShoppingCard card = new ShoppingCard();
        card.addItem(OrderItem.builder()
                .description("Xiaomi Note 9 Pro")
                .price(new BigDecimal(16350))
                .quantity(1)
                .build()
        );
    }

    /**
     * Creator
     */
    private static class ShoppingCard {
        private final List<OrderItem> items = new ArrayList<>();

        public ShoppingCard() {
        }

        /**
         * Метод creator
         */
        public void addItem(OrderItem orderItem) {
            //Not implemented yet
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
