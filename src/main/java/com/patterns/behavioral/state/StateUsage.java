package com.patterns.behavioral.state;

import lombok.Getter;
import lombok.Setter;

public class StateUsage {
    public static void main(String[] args) {
        StateUsage executor = new StateUsage();
        executor.execute();
    }

    private void execute() {
        Developer developer = new Developer();
        developer.cook();
        developer.eat();
        developer.think();
        developer.makeCode();
        developer.think();
        developer.makeCode();
        developer.sleep();
    }
}

@Setter
@Getter
class Developer implements DeveloperState {
    DeveloperState state;

    public Developer() {
        this.state = new DeveloperSleeping(this);
    }

    @Override
    public void sleep() {
        this.state.sleep();
    }

    @Override
    public void cook() {
        this.state.cook();
    }

    @Override
    public void eat() {
        this.state.eat();
    }

    @Override
    public void makeCode() {
        this.state.makeCode();
    }

    @Override
    public void think() {
        this.state.think();
    }
}

interface DeveloperState {
    void sleep();
    void cook();
    void eat();
    void makeCode();
    void think();
}


class DeveloperSleeping extends AbstractDeveloperState {

    public DeveloperSleeping(Developer developer) {
        super(developer);
    }

    @Override
    public void sleep() {
        System.out.println("Sleep...");
    }
}

abstract class AbstractDeveloperState implements DeveloperState {
    Developer developer;

    public AbstractDeveloperState(Developer developer) {
        this.developer = developer;
    }

    @Override
    public void sleep() {
        this.developer.state = new DeveloperSleeping(developer);
        this.developer.state.sleep();
    }

    @Override
    public void cook() {
        this.developer.state = new DeveloperCooking(developer);
        this.developer.state.cook();
    }

    @Override
    public void eat() {
        this.developer.state = new DeveloperEating(developer);
        this.developer.state.eat();
    }

    @Override
    public void makeCode() {
        this.developer.state = new DeveloperMakingCode(developer);
        this.developer.state.makeCode();
    }

    @Override
    public void think() {
        this.developer.state = new DeveloperThinking(developer);
        this.developer.state.think();
    }
}

class DeveloperCooking extends AbstractDeveloperState {

    public DeveloperCooking(Developer developer) {
        super(developer);
    }

    @Override
    public void cook() {
        System.out.println("Cook...");
    }
}

class DeveloperEating extends AbstractDeveloperState {

    public DeveloperEating(Developer developer) {
        super(developer);
    }

    @Override
    public void eat() {
        System.out.println("Eat...");
    }
}

class DeveloperMakingCode extends AbstractDeveloperState {

    public DeveloperMakingCode(Developer developer) {
        super(developer);
    }

    @Override
    public void makeCode() {
        System.out.println("Make code...");
    }
}

class DeveloperThinking extends AbstractDeveloperState {

    public DeveloperThinking(Developer developer) {
        super(developer);
    }

    @Override
    public void think() {
        System.out.println("Think...");
    }
}