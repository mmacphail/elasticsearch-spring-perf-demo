package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    FlowRepository flowRepository;

    @PostConstruct
    public void init() {
        LOGGER.info("Start");
        //.subscribe(flow -> LOGGER.info("Persisted flow"))
        List<Future<Flow>> flows = IntStream.range(0, 10_000)
                .mapToObj(i -> {
                    return asyncTaskExecutor.submit(() -> {
                        Flow flow = flowRepository.save(Flow.random()).block();
                        return flow;
                        //.subscribe(flow -> LOGGER.info("Persisted flow"))
                    });
                })
                .collect(Collectors.toList());

        flows.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        LOGGER.info("End");
    }

}
