package com.algoritms.dinner_philosopher.hierarchy_resources;

import com.algoritms.dinner_philosopher.Fork;

public class HierarchyResourcesFork extends Fork {
    private Integer order;

    public HierarchyResourcesFork(String id, Integer order) {
        super(id);
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
