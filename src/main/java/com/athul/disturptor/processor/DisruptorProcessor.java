package com.athul.disturptor.processor;

import com.athul.disturptor.handler.EmployeeIdHandler;
import com.athul.disturptor.handler.EmployeeNameHandler;
import com.athul.disturptor.model.MyEvent;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class DisruptorProcessor {

    @Getter
    private Disruptor<MyEvent> myEventDisruptor;

    @Autowired
    public DisruptorProcessor(final EmployeeIdHandler employeeIdHandler, final EmployeeNameHandler employeeNameHandler)
    {

        this.myEventDisruptor = new Disruptor<>(MyEvent::new, 1024, Executors.defaultThreadFactory(), ProducerType.SINGLE, new SleepingWaitStrategy());
        this.configureHandlers(employeeIdHandler, employeeNameHandler);

    }

    private void configureHandlers(final EmployeeIdHandler employeeIdHandler, final EmployeeNameHandler employeeNameHandler)
    {

        this.myEventDisruptor.handleEventsWith(employeeIdHandler).then(employeeNameHandler);
    }

}
