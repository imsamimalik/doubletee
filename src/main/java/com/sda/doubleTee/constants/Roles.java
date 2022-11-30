package com.sda.doubleTee.constants;

public enum Roles {

    STUDENT("ROLE_STUDENT"),
    ADMIN("ROLE_ADMIN"),
    FACULTY("ROLE_FACULTY");

    private String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
