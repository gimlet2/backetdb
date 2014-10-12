package com.restmonkeys.backetdb.server.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restmonkeys.backetdb.server.exceptions.OperationForbidden;
import com.restmonkeys.backetdb.server.exceptions.ValidationException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Backet implements Serializable, Validatable {
    private Long id;
    private List<JsonObject> items = new ArrayList<>();
    private String uniqueScript;

    public Backet(Long id) {
        this.id = id;
    }

    public Backet(String st) {
        long value = Long.parseLong(st);
        this.id = value;
    }

    @Override
    public Backet get() {
        return (Backet) Validatable.super.get();
    }

    @Override
    public Optional<Long> getId() {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.of(id);
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Backet addItem(JsonObject item) {
        String script = "function isUnique(elm) {" + uniqueScript + "}";
        Boolean result = false;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(script);
            Invocable invocable = (Invocable) engine;
            result = (Boolean) invocable.invokeFunction("isUnique", item);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new OperationForbidden();
        }
        if (result) {
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(item);
        }
        return this;
    }

    @Override
    public Backet validate() throws ValidationException {
        return this;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        if (getId().isPresent()) {
            jsonObject.addProperty("id", this.id);
        }
        JsonArray array = new JsonArray();
        if (items != null) {
            items.forEach(array::add);
        }
        jsonObject.add("items", array);
        return jsonObject.toString();
    }
}
