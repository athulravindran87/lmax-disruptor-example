package com.athul.disturptor.handler;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.MyEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeIdHandler implements WorkHandler<MyEvent> {

    @Override
    public void onEvent(MyEvent myEvent) throws Exception {

        //log.info("Handle Id {}",myEvent.getEmployee().getId());
        DisruptorUtil.employeeIdMap.put(myEvent.getEmployee().getId(), myEvent.getEmployee().getEmpId());
    }
}
