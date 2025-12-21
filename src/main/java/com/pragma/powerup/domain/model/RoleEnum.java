package com.pragma.powerup.domain.model;

public enum RoleEnum {
    ADMIN("ADMINISTRADOR"),
    OWNER("PROPIETARIO"),
    EMPLOYEE("EMPLEADO"),
    CLIENT("CLIENTE");

    private final String dbName;

    RoleEnum(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }
}