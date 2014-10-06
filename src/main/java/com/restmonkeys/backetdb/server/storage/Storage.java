package com.restmonkeys.backetdb.server.storage;

import com.restmonkeys.backetdb.server.model.Storable;

import java.util.Optional;
import java.util.Set;

public interface Storage {
    <T extends Storable> Storable<T> save(Storable<T> object);

    <T extends Storable> Storable<T> get(Optional<Long> id);

    void drop(Optional<Long> id);

    Set<Long> ids();
}
