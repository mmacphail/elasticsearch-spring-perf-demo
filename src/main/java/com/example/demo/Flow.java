package com.example.demo;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "flows")
public class Flow {
    @Id private String id;
    @Field private String name;
    @Field private String type;
    @Field private String alpha;
    @Field private String beta;
    @Field private String gamma;
    @Field private String delta;
    @Field private String epsilon;
    @Field private String teta;
    @Field private String eta;

    public static Flow random() {
        return new Flow(
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false),
                RandomStringUtils.random(15, true, false)
        );
    }

    public Flow() {
    }

    public Flow(String name, String id, String type, String alpha, String beta, String gamma, String delta, String epsilon, String teta, String eta) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.delta = delta;
        this.epsilon = epsilon;
        this.teta = teta;
        this.eta = eta;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAlpha() {
        return alpha;
    }

    public String getBeta() {
        return beta;
    }

    public String getGamma() {
        return gamma;
    }

    public String getDelta() {
        return delta;
    }

    public String getEpsilon() {
        return epsilon;
    }

    public String getTeta() {
        return teta;
    }

    public String getEta() {
        return eta;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public void setBeta(String beta) {
        this.beta = beta;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public void setEpsilon(String epsilon) {
        this.epsilon = epsilon;
    }

    public void setTeta(String teta) {
        this.teta = teta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    @Override
    public String toString() {
        return "Flow{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", alpha='" + alpha + '\'' +
                ", beta='" + beta + '\'' +
                ", gamma='" + gamma + '\'' +
                ", delta='" + delta + '\'' +
                ", epsilon='" + epsilon + '\'' +
                ", teta='" + teta + '\'' +
                ", eta='" + eta + '\'' +
                '}';
    }
}
