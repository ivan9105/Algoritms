package com.patterns.generators.factory_method;

import com.patterns.generators.model.WebButton;

public class WebDialog extends AbstractDialog {
    @Override
    void createButton() {
        this.button = new WebButton();
    }

    @Override
    public void render() {
        //native render code
    }
}
