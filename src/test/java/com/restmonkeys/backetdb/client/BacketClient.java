package com.restmonkeys.backetdb.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.gson.Gson;
import com.restmonkeys.backetdb.server.Application;
import com.restmonkeys.backetdb.server.model.Backet;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class BacketClient {
    public static final String DEFAULT_CONNECTION = "http://localhost:4567/";
    private static BacketClient client = null;
    private final WebTarget http;

    private String connection = DEFAULT_CONNECTION;

    private BacketClient(String connection) {
        this.connection = connection;
        ClientConfig cc = new ClientConfig().register(JacksonJaxbJsonProvider.class);
        http = ClientBuilder.newClient(cc).target(connection);
    }

    public synchronized static BacketClient client(String connection) {
        if (client == null) {
            client = new BacketClient(connection);
        }
        return client;
    }

    public synchronized static BacketClient client() {
        return client(DEFAULT_CONNECTION);
    }

    public List<Backet> getAllBackets() {

        WebTarget path = http.path("/");
        Response response = path.request().accept(MediaType.APPLICATION_JSON).get();
        GenericType<List<Backet>> genericType = new GenericType<List<Backet>>() {
        };
        return response.readEntity(genericType);
    }

    public Backet createBacket() {
        return createBacket("{}");
    }

    public Backet createBacket(String entity) {
        return new Gson().fromJson(http.path("/").request(Application.APPLICATION_JSON)
                .post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class), Backet.class);
    }

    public Backet getBacket(Long id) {
        WebTarget path = http.path("/").path(id.toString());
        Response response = path.request().accept(MediaType.APPLICATION_JSON).get();
        return new Gson().fromJson(response.readEntity(String.class), Backet.class);
    }


    public Backet addItem(Backet backet, String item) {
        String json = http.path("/").path(backet.getId().get().toString()).path("item").request(Application.APPLICATION_JSON)
                .post(Entity.entity(item, MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class);
        return new Gson().fromJson(json, Backet.class);
    }

    public void deleteBacket(Long id) {
        http.path("/").path(id.toString()).request().delete();
    }
}
