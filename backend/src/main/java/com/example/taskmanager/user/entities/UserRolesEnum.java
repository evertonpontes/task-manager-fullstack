package com.example.taskmanager.user.entities;

public enum UserRolesEnum {
    ADMIN, USER;

    public String getAuthority() {
         return "ROLE_" + this.name();
    }
}
