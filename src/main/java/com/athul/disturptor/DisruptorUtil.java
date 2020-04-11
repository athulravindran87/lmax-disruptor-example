package com.athul.disturptor;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DisruptorUtil {

    public Map<Integer, String> employeeIdMap = new HashMap<>();
    public Map<Integer, String> employeeNameMap = new HashMap<>();

    public long startTime = 0L;
    public int limit=0;

    public void clear()
    {
        employeeIdMap = new HashMap<>();
        employeeNameMap = new HashMap<>();
    }
}
