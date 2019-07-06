package com.patterns.generators.model;

public abstract class AbstractCheckbox extends AbstractComponent implements ActionHolder {
    protected Action action;
    protected boolean checked = false;

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public Action getAction() {
        return action;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    //other checkbox's methods
}