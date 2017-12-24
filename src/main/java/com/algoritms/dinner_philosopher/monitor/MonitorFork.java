package com.algoritms.dinner_philosopher.monitor;

import com.algoritms.dinner_philosopher.Fork;

public class MonitorFork extends Fork {
    private boolean availability = true;

    public MonitorFork(String id) {
        super(id);
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
