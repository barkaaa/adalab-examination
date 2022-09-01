package com.adalab.examination.entity;

public class MyPrincipal {
    Integer id;
    String role;

    public Integer getName() {
        return id;
    }

    public void setName(Integer name) {
        this.id = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public MyPrincipal(Integer name, String role) {
        this.id = name;
        this.role = role;
    }
}
