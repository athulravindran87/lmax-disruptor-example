package com.athul.disturptor.producer;

import com.athul.disturptor.model.Employee;
import com.athul.disturptor.model.MyEvent;
import com.athul.disturptor.processor.DisruptorProcessor;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventProducer {

    static final EventTranslatorOneArg<MyEvent, Employee> TRANSLATOR_ONE_ARG = EventProducer::translate;

    private RingBuffer<MyEvent> ringBuffer;

    @Autowired
    public EventProducer(final DisruptorProcessor disruptorProcessor)
    {
        this.ringBuffer = disruptorProcessor.getMyEventDisruptor().start();

    }

    private static void translate(MyEvent myEvent, long l, Employee employee) {

        myEvent.setEmployee(employee);
        myEvent.setSequence(l);
    }

    public void publishEvent(Employee employee)
    {
        this.ringBuffer.publishEvent(TRANSLATOR_ONE_ARG, employee);
    }
}
