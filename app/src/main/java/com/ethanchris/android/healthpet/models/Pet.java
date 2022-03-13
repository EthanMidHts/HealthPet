package com.ethanchris.android.healthpet.models;

public class Pet {
    public static final String FB_PET_NAME = "name";
    public static final String FB_PET_COLOR = "color";

    private String name;
    private String color;

    public Pet(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
