package com.ticticboooom.mods.mm.registration;

public class Registerable<T> {
    private T inner;
    public Registerable(){

    }

    public void set(T inner) {
        this.inner = inner;
    }

    public T get() {
        return inner;
    }
}
