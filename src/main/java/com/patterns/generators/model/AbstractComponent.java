package com.patterns.generators.model;

import static java.util.UUID.randomUUID;

public abstract class AbstractComponent implements Component {
    protected boolean enabled = true;
    protected Listener listener;
    protected String id = randomUUID().toString().replaceAll("-", "");

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
