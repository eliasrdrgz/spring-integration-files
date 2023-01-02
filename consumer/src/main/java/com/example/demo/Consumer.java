package com.example.demo;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.Pollers;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Client;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class Consumer {
    @ServiceActivator(inputChannel = "consumeClient", outputChannel = "clientConsumed")
    public Client processClient(Client process)
    {
        log.info("received {}", process);
        Client other = process.toBuilder().build();
        log.info("generated {}", other);
        return other;
    }    
}
