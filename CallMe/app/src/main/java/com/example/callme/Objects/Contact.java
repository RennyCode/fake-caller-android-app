package com.example.callme.Objects;

public class Contact {
    String name;
    String number;

    public Contact(){}
    public Contact(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName() { return this.name;}
    public String getNumber() { return this.number;}
    public void SetName(String name) { this.name = name;}
    public void SetNumber(String number) { this.number = number;}

}
