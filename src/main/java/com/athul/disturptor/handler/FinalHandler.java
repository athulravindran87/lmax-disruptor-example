package com.athul.disturptor.handler;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.model.MyEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FinalHandler implements WorkHandler<MyEvent> {
    @Override
    public void onEvent(MyEvent myEvent) throws Exception {

        log.debug("Final Employee {}", myEvent.getEmployee().getId());
        DisruptorUtil.finalMap.put(myEvent.getEmployee().getId(), myEvent.getEmployee().getName());
    }
}
