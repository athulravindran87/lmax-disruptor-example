package com.athul.disturptor.service;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RegularService {


    public void kickOffProcess(int limit) throws Exception
    {
        StopWatch stopWatch = new StopWatch();
        List<Employee> employeeList = this.prepareEmployeeList(limit);

        stopWatch.start();
        for(Employee employee: employeeList)
        {
            this.handleId(employee);
            this.handleEmployeeName(employee);
        }
        stopWatch.stop();

        log.info("process completed for {}",employeeList.size());
        log.info("Single Threaded - Total Processing Time: {}", stopWatch.getTotalTimeMillis());
    }

    private void handleId(Employee employee) throws Exception
    {
        //log.info("Handle Id {}",employee.getId());
        DisruptorUtil.employeeIdMap.put(employee.getId(), employee.getEmpId());
    }

    private void handleEmployeeName(Employee employee)
    {
        //log.info("Handle Employee {}",employee.getId());
        DisruptorUtil.employeeIdMap.put(employee.getId(), employee.getName());
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
