package com.grasp;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.cmc.CMCStatusInfoV2;

/**
 * Шаблон сontroller призван решить проблему разделения интерфейса и логики в интерактивном приложении.
 * Это не что иное, как хорошо известный контроллер из MVC парадигмы.
 * Контролер отвечает за обработку запросов и решает кому должен делегировать запросы на выполнение.
 * Если обобщить назначение сontroller, то он должен отвечать за обработку входных системных сообщений.
 */
public class Controller {

    public static void main(String[] args) {
        CalculatorController calculatorController = new CalculatorController(
                new Input(),
                new Input(),
                new Calculator()
        );
    }

    @Data
    @RequiredArgsConstructor
    private static class CalculatorController {
        private final Input firstInput;
        private final Input secondInput;
        private final Calculator calculator;

        void add() {
            double first = firstInput.getValue();
            double second = secondInput.getValue();
            calculator.add(first, second);
        }
    }

    private static class Input {
        double getValue() {
            //not implemented yet
            return 0d;
        }
    }

    private static class Calculator {
        void getTotal() {
            //not implemented yet
        }

        void add(double first, double second) {
            //not implemented yet
        }
    }


}
