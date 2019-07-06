package com.patterns.generators.abstract_factory;

import com.patterns.generators.model.AbstractButton;
import com.patterns.generators.model.AbstractCheckbox;
import com.patterns.generators.model.AbstractTextInput;

/**
 * Абстрактная фабрика компонентов
 * каждая фабрика только знает и создаеет свои продукты
 * фабрики легко взаимозаменять
 */
public abstract class AbstractComponentFactory {
    public abstract AbstractCheckbox createCheckbox();
    public abstract AbstractTextInput createTextInput();
    public abstract AbstractButton createButton();
}
