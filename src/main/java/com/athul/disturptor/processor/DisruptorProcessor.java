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

        this.myEventDisruptor = new Disruptor<>(MyEvent::new, 2048, Executors.defaultThreadFactory(), ProducerType.SINGLE, new SleepingWaitStrategy());
        this.configureHandlers(employeeIdHandler, employeeNameHandler, finalHandler);

    }

    private void configureHandlers(final EmployeeIdHandler employeeIdHandler, final EmployeeNameHandler employeeNameHandler, final FinalHandler finalHandler) {

        this.myEventDisruptor.handleEventsWithWorkerPool(createArrayOfHandlers(150))
                .thenHandleEventsWithWorkerPool(createEmployeeNameHandlers(250))
                .thenHandleEventsWithWorkerPool(createFinalNameHandlers(150));
    }

    private EmployeeIdHandler[] createArrayOfHandlers(int size) {
        EmployeeIdHandler[] employeeIdHandlers = new EmployeeIdHandler[size];
        for (int i = 0; i < size; i++) {
            employeeIdHandlers[i] = new EmployeeIdHandler();
        }
        return employeeIdHandlers;
    }

    private EmployeeNameHandler[] createEmployeeNameHandlers(int size) {
        EmployeeNameHandler[] employeeNameHandlers = new EmployeeNameHandler[size];
        for (int i = 0; i < size; i++) {
            employeeNameHandlers[i] = new EmployeeNameHandler();
        }
        return employeeNameHandlers;
    }

    private FinalHandler[] createFinalNameHandlers(int size) {
        FinalHandler[] finalHandlers = new FinalHandler[size];
        for (int i = 0; i < size; i++) {
            finalHandlers[i] = new FinalHandler();
        }
        return finalHandlers;
    }

}
