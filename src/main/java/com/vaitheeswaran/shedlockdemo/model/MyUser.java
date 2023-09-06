package com.vaitheeswaran.shedlockdemo.model;

import java.io.Serializable;

public class MyUser implements Serializable {

    private Long id;
    private String name;
    private String surname;
    private String address;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // getter, setter, equals and hashcode
}
