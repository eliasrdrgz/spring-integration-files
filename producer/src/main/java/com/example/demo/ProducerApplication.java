package com.example.demo;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

import com.example.demo.domain.Client;

@SpringBootApplication
@EnableIntegrationGraphController
@EnableIntegrationManagement
@EnableIntegration
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	public IntegrationFlow generateFiles(
		@Value("${file.input.folder:/tmp/integration}/inbound") File source)
	{
		return IntegrationFlows.fromSupplier(() -> Client.random(), e -> e.poller(Pollers.fixedDelay(10000)))
		.transform(Transformers.toJson())
		.log()
		.handle(Files.outboundAdapter(source).autoCreateDirectory(true))
		.get();
	}


}
