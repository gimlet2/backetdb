package com.restmonkeys.backetdb.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.restmonkeys.backetdb.server.exceptions.ValidationException;
import com.restmonkeys.backetdb.server.scripting.Eval;
import com.restmonkeys.backetdb.server.scripting.EvalFactory;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Backet implements Serializable, Validatable {
    private static final long serialVersionUID = -6751168190572034259L;
    private Long id;
    private List<String> items = Collections.synchronizedList(new ArrayList<>());
    private Function uniqueScript = new Function();
    private List<Function> aggregates = new ArrayList<>();
    private Map<String, Object> results = Collections.synchronizedMap(new HashMap<>());

    public Backet() {
    }

    public Backet(Long id) {
        this.id = id;
    }

    public Backet(String st) {
        this.id = Long.parseLong(st);
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

    public synchronized Backet addItem(String item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        if (results == null) {
            results = new HashMap<>();
        }
        items.add(item);

        Eval eval = EvalFactory.getEval();
        Gson gson = new Gson();
        aggregates.forEach((f) -> {
            Object prev = results.get(f.getName());
            results.put(f.getName(), eval.eval(f, gson.fromJson(join(items), ArrayList.class), prev, item));
        });
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
        JsonArray itemsArray = new JsonArray();
        if (items != null) {
            JsonParser jsonParser = new JsonParser();
            items.forEach(e -> itemsArray.add(jsonParser.parse(e)));
        }
        jsonObject.add("items", itemsArray);
//        JsonArray resultsArray = new JsonArray();
//        if (results != null) {
//            results.forEach(A -> {
//                JsonParser jsonParser = new JsonParser();
//
//                JsonObject element = new JsonObject();
//                element.add(jsonParser.parse(A));
//                resultsArray.add(element);
//            });
//        }
        jsonObject.add("results", new JsonParser().parse(new Gson().toJson(results)));
        return jsonObject.toString();
    }

    private String join(List<String> list) {
        StringBuilder sb = new StringBuilder("[");
        if (items != null) {
            items.forEach(s -> sb.append(StringEscapeUtils.unescapeJson(s)).append(","));
        }
        if (list.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.append("]").toString();
    }

    public List<String> getItems() {
        return items;
    }

    public Map<String, Object> getResults() {
        return results;
    }
}
