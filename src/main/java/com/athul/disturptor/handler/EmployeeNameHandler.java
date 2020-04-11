package com.athul.disturptor.handler;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.MyEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeNameHandler implements EventHandler<MyEvent> {

    @Override
    public void onEvent(MyEvent myEvent, long l, boolean b) throws Exception {
        DisruptorUtil.employeeNameMap.put(myEvent.getEmployee().getId(), myEvent.getEmployee().getName());

        if(myEvent.getEmployee().getId() == DisruptorUtil.limit-1)
        {
            log.info("Ring Buffer - Total Processing Time: {}", (System.currentTimeMillis() - DisruptorUtil.startTime));
        }
    }
}
