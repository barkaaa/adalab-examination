package com.adalab.examination.entity;

import org.apache.shiro.authc.AuthenticationToken;

public class StudentInfoToken implements AuthenticationToken {
    String name;
    String ID;
    String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StudentInfoToken(String name, String ID, String role) {
        this.name = name;
        this.ID = ID;
        this.role = role;
    }

    @Override
    public Object getPrincipal() {
        return name;
    }

    @Override
    public Object getCredentials() {
        return ID;
    }
}
