package com.restmonkeys.backetdb.server.model;

import com.google.gson.JsonObject;
import com.restmonkeys.backetdb.server.exceptions.ValidationException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Backet implements Serializable, Validatable {
    private Optional<Long> id = Optional.empty();
    private List<JsonObject> items = new ArrayList<>();

    public Backet(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public Backet(String id) {
        this.id = Optional.ofNullable(Long.parseLong(id));
    }


    @Override
    public Backet get() {
        return (Backet) Validatable.super.get();
    }

    @Override
    public Optional<Long> getId() {
        if (id == null) {
            id = Optional.empty();
        }
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public Backet addItem(JsonObject item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        return this;
    }

    @Override
    public Backet validate() throws ValidationException {
        return this;
    }
}
