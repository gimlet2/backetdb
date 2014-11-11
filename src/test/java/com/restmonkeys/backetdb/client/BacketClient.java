package com.restmonkeys.backetdb.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.restmonkeys.backetdb.server.model.Backet;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
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

    public void deleteBacket(Long id) {
        http.path("/").path(id.toString()).request().delete();
    }
}