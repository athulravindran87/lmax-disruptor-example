package com.athul.disturptor.service;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.Employee;
import com.athul.disturptor.producer.EventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DisruptorService {

    @Autowired
    private EventProducer eventProducer;

    public void kickOffRingBuffer(int limit)
    {
        List<Employee> employeeList = this.prepareEmployeeList(limit);

        DisruptorUtil.startTime = System.currentTimeMillis();
        DisruptorUtil.limit = limit;
        for(Employee employee: employeeList)
        {
            this.eventProducer.publishEvent(employee);
        }
        log.info("process completed for {}",employeeList.size());

    }


    private List<Employee> prepareEmployeeList(int limit)
    {
        List<Employee> employeeList = new ArrayList<>();

        for(int i=0; i<limit; i++)
        {
            employeeList.add(Employee.builder().id(i).empId("EMPID"+i).name("NAME"+i).build());
        }

        return employeeList;
    }

}
