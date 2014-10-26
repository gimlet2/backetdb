package com.restmonkeys.backetdb.server;

import com.restmonkeys.backetdb.server.model.Backet;
import com.restmonkeys.backetdb.server.storage.StorageFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

public class Application {

    public static final String APPLICATION_JSON = "application/json";
    public static final int CREATED = 201;

    public Application() {
        init();
    }

    private void init() {
        JsonTransformer<Backet> bucketTransformer = new JsonTransformer<>();

        /**
         *  Get list of all backet ids
         */
        get("/", (req, res) -> {
            setCommonResponseHeaders(res);
            return StorageFactory.getStorage().ids();
        }, bucketTransformer);

        /**
         * Get bucket by id
         */
        get("/:id", (req, res) -> {
            setCommonResponseHeaders(res);
            return new Backet(id(req)).get();
        }, bucketTransformer);

        /**
         * Get aggregates results for bucket
         */
        get("/:id/aggregates", (req, res) -> {
            setCommonResponseHeaders(res);
            return "";
        }, bucketTransformer);

        /**
         * Create backet
         */
        post("/", APPLICATION_JSON, (req, res) -> {
            setCommonResponseHeaders(res);
            res.status(CREATED);
            return bucketTransformer.unmarshal(req.body(), Backet.class)
                    .validate()
                    .save();
        }, bucketTransformer);

        /**
         * Add backet member
         */
        post("/:id/item", APPLICATION_JSON, (req, res) -> {
            setCommonResponseHeaders(res);
            return new Backet(id(req)).get()
                    .addItem(req.body())
                    .save();
        }, bucketTransformer);

        /**
         * Drop backet
         */
        delete("/:id", (req, res) -> {
            setCommonResponseHeaders(res);
            new Backet(id(req)).drop();
            return "";
        }, bucketTransformer);

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body("Something bad happened" + e);
            e.printStackTrace();
        });

    }

    private String id(Request req) {
        return req.params(":id");
    }

    private void setCommonResponseHeaders(Response res) {
        res.type(APPLICATION_JSON);
    }

}
