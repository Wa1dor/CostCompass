package com.waldor.costcompass.dev.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waldor.costcompass.dev.services.DevAutonomousAgentService;

@RestController
@RequestMapping("/api/costcompass/dev/agent")
public class DevAgentController {

    private final DevAutonomousAgentService devAutonomousAgentService;

    public DevAgentController(DevAutonomousAgentService devAutonomousAgentService) {
        this.devAutonomousAgentService = devAutonomousAgentService;
    }

    @PostMapping("/run")
    public String run() {
        return devAutonomousAgentService.runAutonomousScan();
    }
}