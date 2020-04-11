package com.athul.disturptor.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Employee implements Serializable {

    private int id;
    private String name;
    private String empId;
}
