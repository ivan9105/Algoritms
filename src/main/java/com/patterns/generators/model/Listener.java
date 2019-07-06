package com.patterns.generators.model;

public interface Listener {
    void onChange(Event event);
    void onClick(Event event);
    void onMouseOver(Event event);
    void onMouseOut(Event event);
    void onKeyDown(Event event);
    void onInit(Event event);
}
