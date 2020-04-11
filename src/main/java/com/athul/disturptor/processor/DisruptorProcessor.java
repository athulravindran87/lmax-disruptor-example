package com.athul.disturptor.processor;

import com.athul.disturptor.handler.EmployeeIdHandler;
import com.athul.disturptor.handler.EmployeeNameHandler;
import com.athul.disturptor.handler.FinalHandler;
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
    public DisruptorProcessor(final EmployeeIdHandler employeeIdHandler, final EmployeeNameHandler employeeNameHandler, final FinalHandler finalHandler)
    {

        this.myEventDisruptor = new Disruptor<>(MyEvent::new, 65536, Executors.defaultThreadFactory(), ProducerType.SINGLE, new SleepingWaitStrategy());
        this.configureHandlers(employeeIdHandler, employeeNameHandler, finalHandler);

    }

    private void configureHandlers(final EmployeeIdHandler employeeIdHandler, final EmployeeNameHandler employeeNameHandler, final FinalHandler finalHandler)
    {

        this.myEventDisruptor.handleEventsWithWorkerPool(employeeIdHandler)
                .thenHandleEventsWithWorkerPool(employeeNameHandler)
                .thenHandleEventsWithWorkerPool(finalHandler);
    }

    private EmployeeIdHandler[] createArrayOfHandlers(int size) {
        EmployeeIdHandler[] employeeIdHandlers = new EmployeeIdHandler[5];
        for (int i = 0; i < size; i++) {
            employeeIdHandlers[i] = new EmployeeIdHandler();
        }
        return employeeIdHandlers;
    }

}
