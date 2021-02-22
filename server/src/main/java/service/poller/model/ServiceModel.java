package service.poller.model;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface ServiceModel {
    Future<List<Service>> getServicesByUser(String user);
    Future<List<Service>> getAllServices();
    Future<Boolean> addService(String user, String name, String url);
    Future<Boolean> setStatus(String user, int id, int status);
    Future<Boolean> deleteService(String user, int id);

     class Service {
        private final String user;
        private final String name;
        private final String url;
        private final String created;
        private final String updated;
        private final int id;
        private final int status;

         Service(String user, String name, String url, String created, String updated, int id, int status) {
            this.user = user;
            this.name = name;
            this.url = url;
            this.created = created;
            this.updated = updated;
            this.id = id;
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

         public int getId() {
            return id;
         }

         public String getUser() {
             return user;
         }

        public JsonObject toJson() {
            return new JsonObject()
                    .put("name", name)
                    .put("url", url)
                    .put("created", created)
                    .put("updated", updated)
                    .put("status", status)
                    .put("id", id);
        }
    }
}
