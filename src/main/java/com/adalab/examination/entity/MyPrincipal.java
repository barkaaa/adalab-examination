package com.adalab.examination.entity;

public class MyPrincipal {
    Integer id;
    String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
