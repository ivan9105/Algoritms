package com.grasp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Pure Fabrication или чистая выдумка, или чистое синтезирование.
 * Здесь суть в выдуманном объекте.
 * Аналогом может быть шаблон Service (сервис) в парадигме DDD.
 *
 * Какую проблему решает Pure Fabrication?
 *
 * Уменьшает зацепление (Low Coupling);
 * Повышает связанность (High Cohesion);
 * Упрощает повторное использование кода.
 */
public class PureFabrication {
    public static void main(String[] args) {

    }

    //TODO ICacheStore - redis facade implementation
    //TODO IForeignExchange - наш обменник
    //TODO если кеш пуст по ключу, то возвращаем данные из внешнего сервиса ExchangeExternalService class

    private interface IForeignExchange {
        List<ConversionRate> getConversionRates();
    }

    private interface ICacheStore {
        List<ConversionRate> getConversionRates();
    }

    @RequiredArgsConstructor
    @Data
    private static class ConversionRate {
        private final String currentCurrency;
        private final String exchangeCurrency;
        private final BigDecimal rate;
    }
}
