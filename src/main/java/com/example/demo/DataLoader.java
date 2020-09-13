package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private FlowRepository flowRepository;

    @PostConstruct
    public void init() throws InterruptedException {
        UnicastProcessor<Flow> processor = UnicastProcessor.create();
        FluxSink<Flow> fluxSink = processor.sink();

        Flux<Flow> result = processor.bufferTimeout(1000, Duration.ofMillis(1000))
                .delayElements(Duration.ofMillis(100))
                .flatMap(this::persistFlows);

        LOGGER.info("Started result");
        result.subscribe(
                flow -> {},
                error -> LOGGER.error("oh no there is an error ! \n\n{}", error.getMessage()),
                () -> LOGGER.info("Completed result")
        );

        for(int i=0; i<10; i++) {
            IntStream.range(0, 100_000)
                    .mapToObj(j -> Flow.random())
                    .forEach(fluxSink::next);
            LOGGER.info("pack sent");
            Thread.sleep(15000);
        }

        fluxSink.complete();
    }

    private Flux<Flow> persistFlows(List<Flow> flows) {
        return flowRepository.saveAll(flows);
    }
}
