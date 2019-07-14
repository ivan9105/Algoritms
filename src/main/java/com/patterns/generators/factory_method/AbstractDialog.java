package com.patterns.generators.factory_method;

import com.patterns.generators.model.AbstractButton;
import com.patterns.generators.model.AbstractComponent;

public abstract class AbstractDialog extends AbstractComponent {
    protected AbstractButton button;

    public AbstractDialog() {
        createButton();
    }

    /**
     * Разница между фабричным методом и абстрактной фабрикой в том что в одном случае у нас один метод и у нас одно семейство производных классов
     * в абрактной фабрике их может быть много
     *
     * простая фабрика просто создает объект по входным п-рам
     */
    //factory method
    abstract void createButton();

    public AbstractButton getButton() {
        return button;
    }
}
