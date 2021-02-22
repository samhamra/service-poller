package service.poller.web;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import service.poller.model.ServiceModel;

import java.util.List;
import java.util.stream.Collectors;

public class WebService {
    private static final int DEFAULT_PORT = 9090;
    private final Router router;
    private final ServiceModel model;

    public WebService(Vertx vertx, ServiceModel model, int port) {
        this.model = model;
        HttpServer server = vertx.createHttpServer();
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        setupCors();
        setupRoutes();
        server.requestHandler(router);
        server.listen(port);
    }

    public WebService(Vertx vertx, ServiceModel model) {
        this(vertx, model, DEFAULT_PORT);
    }

    private void setupCors() {
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.DELETE)
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type"));
    }

    private void setupRoutes() {
        router.get("/:user/services").respond(ctx ->
        model.getServicesByUser(ctx.pathParam("user")).transform(asyncResult -> {
                    if (asyncResult.succeeded()) {
                        System.out.println(("GET"));
                        List<JsonObject> jsonarr = asyncResult.result().stream().map(ServiceModel.Service::toJson).collect(Collectors.toList());
                        return Future.succeededFuture(jsonarr);
                    } else {
                        System.out.println("BUG");
                        return Future.failedFuture("Could not get service");
                    }
                }));

        router.post("/:user/services").consumes("*/json").respond(ctx -> {
            String user = ctx.pathParam("user");
            JsonObject jsonObject = ctx.getBodyAsJson();
            String name = jsonObject.getString("name");
            String url = jsonObject.getString("url");

            return model.addService(user, name, url).transform(asyncResult -> {
                if (asyncResult.succeeded()) {
                    return Future.succeededFuture();
                } else {
                    return Future.failedFuture("Could not get service");
                }
            });
        });

        router.delete("/:user/services/:serviceId").respond(ctx -> {
            String user = ctx.pathParam("user");
            int id = Integer.parseInt(ctx.pathParam("serviceId"));
            return model.deleteService(user, id).transform(asyncResult -> {
                if (asyncResult.succeeded()) {
                    return Future.succeededFuture();
                } else {
                    return Future.failedFuture("Could not get service");
                }
            });
        });
    }
}
