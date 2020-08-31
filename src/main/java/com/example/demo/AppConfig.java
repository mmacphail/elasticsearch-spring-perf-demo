package com.example.demo;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.resources.LoopResources;

import javax.net.ssl.SSLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean("my-nio-event")
    NioEventLoopGroup getNioEventLoopGroup() {
        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("TracesThread-%d")
                .priority(Thread.MAX_PRIORITY)
                .build();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );

        return new NioEventLoopGroup(5, executor);
    }

    @Bean("my-rrf")
    public ReactorResourceFactory reactorResourceFactory(@Qualifier("my-nio-event") NioEventLoopGroup group) {
        ReactorResourceFactory f= new ReactorResourceFactory();
        f.setLoopResources(b -> group);
        f.setUseGlobalResources(false);
        return f;
    }

    @Bean("my-rchc")
    public ReactorClientHttpConnector reactorClientHttpConnector(@Qualifier("my-rrf") ReactorResourceFactory r) throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        return new ReactorClientHttpConnector(r, m -> m.secure(t -> t.sslContext(sslContext)));
    }

    @Bean("my-webClient")
    public WebClient webClient(@Qualifier("my-rchc") ReactorClientHttpConnector r) {
        return WebClient.builder().baseUrl("http://localhost:9200").clientConnector(r).build();
    }

}
