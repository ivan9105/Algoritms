package com.patterns.generators.model;

public interface Component {
    void render();
    void setListener(Listener listener);
    Listener getListener();
    void setEnabled(boolean isEnabled);
    boolean isEnabled();
    void setId(String id);
    String getId();
}
