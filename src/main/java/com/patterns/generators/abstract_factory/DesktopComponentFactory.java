package com.patterns.generators.abstract_factory;

import com.patterns.generators.model.*;

public class DesktopComponentFactory extends AbstractComponentFactory {
    @Override
    public AbstractCheckbox createCheckbox() {
        return new DesktopCheckbox();
    }

    @Override
    public AbstractTextInput createTextInput() {
        return new DesktopTextInput();
    }

    @Override
    public AbstractButton createButton() {
        return new DesktopButton();
    }
}
