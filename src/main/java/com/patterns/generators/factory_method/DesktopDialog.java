package com.patterns.generators.factory_method;

import com.patterns.generators.model.DesktopButton;

public class DesktopDialog extends AbstractDialog {
    @Override
    void createButton() {
        this.button = new DesktopButton();
    }

    @Override
    public void render() {
        //native render code
    }
}
