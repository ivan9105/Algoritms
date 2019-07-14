package com.patterns.generators.abstract_factory;

import com.patterns.generators.model.*;

public class WebComponentFactory extends AbstractComponentFactory {
    @Override
    public AbstractCheckbox createCheckbox() {
        return new WebCheckbox();
    }

    @Override
    public AbstractTextInput createTextInput() {
        return new WebTextInput();
    }

    @Override
    public AbstractButton createButton() {
        return new WebButton();
    }
}
