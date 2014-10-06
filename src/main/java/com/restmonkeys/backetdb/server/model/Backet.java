package com.restmonkeys.backetdb.server.model;

import java.io.Serializable;

public class Backet implements Serializable {
    private Long id;
    private String name;

    public Backet(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
