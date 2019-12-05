package com.classes;

public class Couple<T, O> {

    private T first;
    private O second;

    public Couple(T first, O second){
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public O getSecond() {
        return second;
    }
}
