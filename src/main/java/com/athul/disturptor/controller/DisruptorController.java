package com.athul.disturptor.controller;

import com.athul.disturptor.DisruptorUtil;
import com.athul.disturptor.service.DisruptorService;
import com.athul.disturptor.service.RegularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DisruptorController {

    @Autowired
    private DisruptorService disruptorService;

    @Autowired
    private RegularService regularService;

    @GetMapping("/kickoff-disruptor")
    public void kickOffProcessing(@RequestParam int limit)
    {
        DisruptorUtil.clear();
        this.disruptorService.kickOffRingBuffer(limit);
    }

    @GetMapping("/kickoff-single-threaded")
    public void kickOffSingleThreaded(@RequestParam int limit) throws Exception
    {
        DisruptorUtil.clear();
        this.regularService.kickOffProcess(limit);
    }

    @GetMapping("/getIdMap")
    public Map<Integer, String> getIdMap()
    {
        return DisruptorUtil.employeeIdMap;
    }

    @GetMapping("/getNameMap")
    public Map<Integer, String> getNameMap()
    {
        return DisruptorUtil.employeeNameMap;
    }
}
