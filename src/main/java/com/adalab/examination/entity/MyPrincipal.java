package com.adalab.examination.entity;

public class MyPrincipal {
    String name;
    String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public MyPrincipal(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
