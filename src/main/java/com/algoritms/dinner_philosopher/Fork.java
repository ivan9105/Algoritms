package com.algoritms.dinner_philosopher;

public class Fork implements Comparable<Fork> {
    private String id;

    public Fork(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int compareTo(Fork other) {
        return this.getId().compareTo(other.getId());
    }
}
