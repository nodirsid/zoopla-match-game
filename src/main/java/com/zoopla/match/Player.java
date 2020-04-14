package com.zoopla.match;

import java.util.Stack;

public class Player {
    private String name;
    private Stack<Card> allocatedCards;

    public Player(String name) {
        this.name = name;
        this.allocatedCards = new Stack<Card>();
    }

    public String getName() {
        return name;
    }

    public Stack<Card> getAllocatedCards() {
        return allocatedCards;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
