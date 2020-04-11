package com.athul.disturptor.handler;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.MyEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeIdHandler implements EventHandler<MyEvent> {

    @Override
    public void onEvent(MyEvent myEvent, long l, boolean b) throws Exception {
        DisruptorUtil.employeeIdMap.put(myEvent.getEmployee().getId(), myEvent.getEmployee().getEmpId());
    }
}
