package com.pragma.powerup.domain.model;

public class EmployeePerformance {
    private Long employeeId;
    private Double averageDurationInMinutes;

    public EmployeePerformance(Long employeeId, Double averageDurationInMinutes) {
        this.employeeId = employeeId;
        this.averageDurationInMinutes = averageDurationInMinutes;
    }

    public EmployeePerformance() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Double getAverageDurationInMinutes() {
        return averageDurationInMinutes;
    }

    public void setAverageDurationInMinutes(Double averageDurationInMinutes) {
        this.averageDurationInMinutes = averageDurationInMinutes;
    }
}

