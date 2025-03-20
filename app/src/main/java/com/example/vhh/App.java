package com.example.vhh;

public class App {
    private String name;
    private int iconResId;

    public App(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}
