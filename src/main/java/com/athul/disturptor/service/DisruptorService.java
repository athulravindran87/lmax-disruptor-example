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

    public void kickOffRingBuffer(int limit) {
        List<Employee> employeeList = this.prepareEmployeeList(limit);

        DisruptorUtil.startTime = System.currentTimeMillis();
        DisruptorUtil.limit = limit;
        for (Employee employee : employeeList) {
            this.eventProducer.publishEvent(employee);
        }
        log.info("process completed for {}", employeeList.size());
        this.checkDisruptorTime();

    }

    public boolean checkDisruptorTime() {
        while (true) {
            log.info("final map size: {}", DisruptorUtil.finalMap.size());

            if (DisruptorUtil.finalMap.size() > 90000) {
                log.info("Ring Buffer - Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
                return true;
            }
            try {
                Thread.sleep(500);
                log.debug("current map size: {}", DisruptorUtil.finalMap.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private List<Employee> prepareEmployeeList(int limit) {
        List<Employee> employeeList = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            employeeList.add(Employee.builder().id(i).empId("EMPID" + i).name("NAME" + i).build());
        }

        return employeeList;
    }

}
