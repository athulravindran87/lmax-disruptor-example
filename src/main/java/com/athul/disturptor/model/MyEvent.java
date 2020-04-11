package com.athul.disturptor.model;

import lombok.Data;

@Data
public class MyEvent {

    private long sequence;
    private Employee employee;
}
