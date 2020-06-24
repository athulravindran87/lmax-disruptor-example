package com.athul.disturptor.service;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class RegularService {

    public void kickOffSingleThreadedProcess(int limit) throws Exception {

        List<Employee> employeeList = this.prepareEmployeeList(limit);
        DisruptorUtil.limit = limit;
        DisruptorUtil.startTime = System.currentTimeMillis();

        employeeList.stream().forEach(this::hitMainProcess);
        if (DisruptorUtil.finalMap.size() == DisruptorUtil.limit) {
            log.info("Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
        }

        log.info("Single process completed for {}", employeeList.size());

    }


    public void kickOffParallelStreamProcess(int limit) throws Exception {

        List<Employee> employeeList = this.prepareEmployeeList(limit);
        DisruptorUtil.limit = limit;
        DisruptorUtil.startTime = System.currentTimeMillis();

        employeeList.parallelStream().forEach(this::hitMainProcess);


        log.info("Parallel Stream process completed for {}", employeeList.size());

        while (1 == 1) {
            if (DisruptorUtil.finalMap.size() > 90000) {
                log.info("Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
                break;
            }
            try {
                Thread.sleep(500);
                log.info("final map size: {}", DisruptorUtil.finalMap.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void kickOffExecutorService(int limit, int threads) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        List<Employee> employeeList = this.prepareEmployeeList(limit);
        DisruptorUtil.limit = limit;
        DisruptorUtil.startTime = System.currentTimeMillis();

        for (Employee employee : employeeList) {
            executorService.execute(() -> hitMainProcess(employee));
        }
        log.info("Executor Service process completed for {}", employeeList.size());

        while (1 == 1) {
            if (DisruptorUtil.finalMap.size() > 90000) {
                log.info("Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
                break;
            }
            try {
                Thread.sleep(500);
                log.debug("current map size: {}", DisruptorUtil.finalMap.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();


    }

    public void kickOffExecutorServiceAll(int limit) throws Exception {
        ExecutorService executorService = Executors.newWorkStealingPool();

        List<Employee> employeeList = this.prepareEmployeeList(limit);
        DisruptorUtil.limit = limit;
        DisruptorUtil.startTime = System.currentTimeMillis();

        for (Employee employee : employeeList) {
            executorService.execute(() -> hitMainProcess(employee));
        }

        while (1 == 1) {
            if (DisruptorUtil.finalMap.size() > 90000) {
                log.info("Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
                break;
            }
            try {
                Thread.sleep(500);
                //log.info("final map size: {}", DisruptorUtil.finalMap.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

        log.info("Executor Service process completed for {}", employeeList.size());

    }

    private void hitMainProcess(Employee employee) {
        try {

            //log.info("Processing employee {} on thread: {}", employee.getId(), Thread.currentThread().getName());
            this.handleId(employee);
            this.handleEmployeeName(employee);
            this.finalStep(employee);
        } catch (Exception e) {
            log.error("Error in main process ", e);
        }
    }

    private synchronized void handleId(Employee employee) {
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
