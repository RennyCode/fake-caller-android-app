package com.example.callme.Objects;

public class User {
    private String text;
    private Contact[] contacts;
    public User() {
        // Default constructor required for Firebase
    }
    public User(String text, Contact[] contacts) {
        this.text = text;
        this.contacts = contacts;
    }

}
