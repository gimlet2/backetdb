package com.restmonkeys.backetdb.server.storage;

import com.restmonkeys.backetdb.server.model.Storable;

public interface Storage {
    <T extends Storable> Storable<T> save(Storable<T> object);

    <T extends Storable> Storable<T> get(Long id);
}
