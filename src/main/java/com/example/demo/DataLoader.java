package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    @Qualifier("my-webClient")
    private WebClient webClient;

    @PostConstruct
    public void init() {
        LOGGER.info("Start");

        List<Mono<ClientResponse>> flows = IntStream.range(0, 10_000)
                .mapToObj(i -> persistRandomFlow())
                .collect(Collectors.toList());

        flows.forEach(flow -> {
            ClientResponse r = flow.block();
            if(!r.statusCode().is2xxSuccessful()) {
                r.bodyToFlux(String.class).subscribe(body -> {
                    LOGGER.error("Received {} status: {}", r.statusCode(), body);
                });
            }
        });

        LOGGER.info("End");
    }

    private Mono<ClientResponse> persistRandomFlow() {
        Flow flow = Flow.random();

        return webClient.post()
                .uri("/flows/_doc")
                .body(BodyInserters.fromValue(flow))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange();
    }
}
