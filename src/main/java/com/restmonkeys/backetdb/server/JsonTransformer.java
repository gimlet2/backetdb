package com.restmonkeys.backetdb.server;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer<T> implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return model.toString();
    }

    public T unmarshal(String st, Class<T> clazz) {
        return gson.fromJson(st, clazz);
    }

}
