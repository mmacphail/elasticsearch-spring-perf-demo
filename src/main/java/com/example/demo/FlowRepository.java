package com.example.demo;

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

public interface FlowRepository extends ReactiveElasticsearchRepository<Flow, String> {
}
