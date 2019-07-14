package com.patterns.generators.model;

public abstract class AbstractTextInput extends AbstractComponent {
    private String value;

    public void clear() {
        this.value = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //other text input's methods
}
