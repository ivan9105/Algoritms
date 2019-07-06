package com.patterns.generators.model;

public abstract class AbstractButton extends AbstractComponent implements ActionHolder {
    protected Action action;

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public Action getAction() {
        return action;
    }

    //other button's methods
}
