package com.athul.disturptor.handler;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.MyEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeNameHandler implements WorkHandler<MyEvent> {

    @Override
    public void onEvent(MyEvent myEvent) throws Exception {

        log.debug("Handle Employee {}", myEvent.getEmployee().getId());
        Thread.sleep(1);
        DisruptorUtil.employeeNameMap.put(myEvent.getEmployee().getId(), myEvent.getEmployee().getName());
    }
}
