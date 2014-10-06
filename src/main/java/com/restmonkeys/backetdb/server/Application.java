package com.restmonkeys.backetdb.server;

import static spark.Spark.*;

public class Application {

    public static final String APPLICATION_JSON = "application/json";

    public Application() {
        init();
    }

    private void init() {

        /**
         *  Get list of all backets
         */
        JsonTransformer transformer = new JsonTransformer();
        get("/", (req, res) -> {
            res.type(APPLICATION_JSON);
            return "Hello";
        }, transformer);

        /**
         * Get bucket by id
         */
        get("/:id", (req, res) -> {
            res.type(APPLICATION_JSON);
            return "";
        }, transformer);

        /**
         * Get aggregates results for bucket
         */
        get("/:id/aggregates", (req, res) -> {
            res.type(APPLICATION_JSON);
            return "";
        }, transformer);

        /**
         * Create backet
         */
        post("/", APPLICATION_JSON, (req, res) -> {
            res.type(APPLICATION_JSON);
            return "";
        }, transformer);

        /**
         * Add backet member
         */
        post("/:id/item", APPLICATION_JSON, (req, res) -> {
            res.type(APPLICATION_JSON);
            return "";
        }, transformer);

        /**
         * Drop backet
         */
        delete("/:id", (req, res) -> {
            res.type(APPLICATION_JSON);
            return "";
        }, transformer);

    }

}
