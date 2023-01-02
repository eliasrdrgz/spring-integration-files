package com.example.demo;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.QueueChannelOperations;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

import com.example.demo.domain.Client;

@EnableIntegration
@EnableIntegrationManagement
@EnableIntegrationGraphController
@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	public IntegrationFlow receive(
		@Value("file:///${producer.source:/tmp/integration}/inbound") File source,
		@Value("file:///${consumer.target:/tmp/integration/}/consumer/files") File consumerFiles)
	{
		return IntegrationFlows.from(
			Files.inboundAdapter(source))
			.log()
			.handle(
				Files.outboundAdapter(consumerFiles)
					.autoCreateDirectory(true)
					.deleteSourceFiles(true))
			.get();
	}

	@Bean
	public IntegrationFlow process(
		@Value("file:///${consumer.target:/tmp/integration/}/consumer/files") File consumerFiles)
	{
		return IntegrationFlows.from(
			Files.inboundAdapter(consumerFiles))
			.transform(Transformers.fromJson(Client.class))
			.log()
			.channel("consumeClient")
			// .handle((p,h) -> File.class.cast(p).delete)
			.get();
	}

	@Bean IntegrationFlow delete()
	{
		return IntegrationFlows.from("clientConsumed")
			.log()
			.handle((p,h) ->((File)h.get("file_originalFile")).delete())
			.log()
			.get();
	}

}
