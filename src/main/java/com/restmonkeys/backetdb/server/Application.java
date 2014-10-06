package com.restmonkeys.backetdb.server;

import com.google.gson.JsonParser;
import com.restmonkeys.backetdb.server.model.Backet;
import spark.Response;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class Application {

    public static final String APPLICATION_JSON = "application/json";

    public Application() {
        init();
    }

    private void init() {
        JsonTransformer<Backet> bucketTransformer = new JsonTransformer<>();

        /**
         *  Get list of all backets
         */
        get("/", (req, res) -> {
            setCommonResponseHeaders(res);
            return "";
        }, bucketTransformer);

        /**
         * Get bucket by id
         */
        get("/:id", (req, res) -> {
            setCommonResponseHeaders(res);
            return new Backet(req.params(":id")).get();
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
            return bucketTransformer.unmarshal(req.body(), Backet.class)
                    .validate()
                    .save();
        }, bucketTransformer);

        /**
         * Add backet member
         */
        post("/:id/item", APPLICATION_JSON, (req, res) -> {
            setCommonResponseHeaders(res);
            return new Backet(req.params(":id")).get()
                    .addItem(new JsonParser().parse(req.body()).getAsJsonObject())
                    .save();
        }, bucketTransformer);

        /**
         * Drop backet
         */
        delete("/:id", (req, res) -> {
            setCommonResponseHeaders(res);
            return "";
        }, bucketTransformer);

    }

    private void setCommonResponseHeaders(Response res) {
        res.type(APPLICATION_JSON);
    }

}
