package service.poller.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import service.poller.model.ServiceModel;

public class PollingController {
    private static final int POLLING_RATE = 1000 * 60;
    private static final int TIME_OUT = 1000 * 10;
    private final ServiceModel model;
    private final WebClient client;

    public PollingController(Vertx vertx, ServiceModel model) {
        this.model = model;
        client = WebClient.create(vertx);
        vertx.setPeriodic(POLLING_RATE, id -> poll());
    }

    private void poll() {
        model.getAllServices().onComplete(ar -> {
            if(ar.succeeded()) {
                ar.result().forEach(this::makeRequest);
            } else {
                System.out.println(ar.cause());
            }
        });
    }

    public void makeRequest(ServiceModel.Service service) {
        client
                .get(80, service.getUrl(), "/")
                .timeout(TIME_OUT)
                .send()
                .onSuccess(response -> {
                    model.setStatus(service.getUser(), service.getId(),  1);
                })
                .onFailure(err -> {
                    model.setStatus(service.getUser(), service.getId(), 0);
                });
    }
}