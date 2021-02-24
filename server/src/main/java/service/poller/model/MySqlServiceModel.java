package service.poller.model;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class MySqlServiceModel implements ServiceModel{
    public final static String INSERT_SERVICE= "INSERT INTO service (username, name, url) VALUES (?, ?, ? )";
    public final static String SELECT_ALL_SERVICES = "SELECT * FROM service";
    public final static String SELECT_ALL_SERVICES_BY_USER = "SELECT * FROM service WHERE username = ?";
    public final static String UPDATE_STATUS_BY_ID = "UPDATE service SET status = ? WHERE username = ? AND id = ?";
    public final static String DELETE_SERVICE = "DELETE FROM service WHERE username = ? AND id = ?";

    private final static String DEFAULT_HOST = "localhost";
    private final static String DEFAULT_DATABASE = "dev";
    private final static String DEFAULT_USER = "root";
    private final static String DEFAULT_PASSWORD = "secret";
    private final static int DEFAULT_PORT = 3309;
    private final MySQLPool client;

    public MySqlServiceModel(String host, int port, String database, String user, String password) {
        client = getDatabaseClient(host, port, database, user, password);
    }

    public MySqlServiceModel() {
        this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE, DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public Future<List<Service>> getAllServices() {
        return client.query(SELECT_ALL_SERVICES).execute().transform(asyncResult -> {
            if(asyncResult.succeeded()) {
                ArrayList<Service> services = new ArrayList<>();
                //borde vara ett set, eller annan datastruktur som är Iterable
                asyncResult.result().forEach(row -> services.add(getService(row)));
                return Future.succeededFuture(services);
            } else {
                return Future.failedFuture("Could not get service");
            }
        });
    }

    public Future<List<Service>> getServicesByUser(String user) {
        return client.preparedQuery(SELECT_ALL_SERVICES_BY_USER).execute(Tuple.of(user)).transform(asyncResult -> {
            if(asyncResult.succeeded()) {
                ArrayList<Service> services = new ArrayList<>();
                asyncResult.result().forEach(row -> services.add(getService(row)));
                return Future.succeededFuture(services);
            } else {
                return Future.failedFuture("Could not get service");
            }
        });
    }

    public Future<Boolean> addService(String user, String name, String url) {
        return client.preparedQuery(INSERT_SERVICE).execute(Tuple.of(user, name, url)).transform(this::booleanHandler);
    }

    public Future<Boolean> deleteService(String user, int id) {
        return client.preparedQuery(DELETE_SERVICE).execute(Tuple.of(user, id)).transform(this::booleanHandler);
    }


    public Future<Boolean> setStatus(String user, int id, int status) {
        return client.preparedQuery(UPDATE_STATUS_BY_ID).execute(Tuple.of(status, user, id)).transform(this::booleanHandler);
    }

    public Future<Boolean> booleanHandler(AsyncResult async) {
        if(async.succeeded()) {
            return Future.succeededFuture(true);
        } else {
            return Future.failedFuture("Could not get service");
        }
    }

    private MySQLPool getDatabaseClient(String host, int port, String database, String user, String password) {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(port)
                .setHost(host)
                .setDatabase(database)
                .setUser(user)
                .setPassword(password);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        return MySQLPool.pool(connectOptions, poolOptions);
    }

    private Service getService(Row row) {
        String user = (String) row.getValue("username");
        String name = (String) row.getValue("name");
        String url = (String) row.getValue("url");
        String created = row.getValue("created").toString();
        String updated = row.getValue("updated").toString();
        int id = (Integer) row.getValue("id");
        int status = (Integer) row.getValue("status");
        return new Service(user, name, url, created, updated, id, status);
    }
}