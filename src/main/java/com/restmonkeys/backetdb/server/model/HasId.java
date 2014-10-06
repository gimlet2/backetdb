package com.restmonkeys.backetdb.server.model;

import java.util.Optional;

public interface HasId {

    public Optional<Long> getId();

    void setId(Long id);
}
