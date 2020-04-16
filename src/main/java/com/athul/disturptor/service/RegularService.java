package com.athul.disturptor.service;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RegularService {


    public void kickOffProcess(int limit) throws Exception {
        List<Employee> employeeList = this.prepareEmployeeList(limit);
        DisruptorUtil.limit = limit;
        DisruptorUtil.startTime = System.currentTimeMillis();

        employeeList.parallelStream().forEach(this::hitMainProcess);
        log.info("process completed for {}", employeeList.size());

    }

    private void hitMainProcess(Employee employee) {
        try {
            this.handleId(employee);
            this.handleEmployeeName(employee);
            this.finalStep(employee);
        } catch (Exception e) {
            log.error("Error in main process ", e);
        }
    }

    private void handleId(Employee employee)
    {
        //log.info("Handle Id {}",employee.getId());
        DisruptorUtil.employeeIdMap.put(employee.getId(), employee.getEmpId());
    }

    private void handleEmployeeName(Employee employee) throws Exception
    {
        Thread.sleep(1);
        //log.info("Handle Employee {}",employee.getId());
        DisruptorUtil.employeeIdMap.put(employee.getId(), employee.getName());
    }

    private void finalStep(Employee employee) {
        //log.info("Handle Employee {}",employee.getId());
        DisruptorUtil.finalMap.put(employee.getId(), employee.getName());
        if (employee.getId() == DisruptorUtil.limit - 1) {
            log.info("Single Threaded - Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
        }
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
