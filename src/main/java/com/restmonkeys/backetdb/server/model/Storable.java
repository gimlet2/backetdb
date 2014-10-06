package com.restmonkeys.backetdb.server.model;

import com.restmonkeys.backetdb.server.storage.StorageFactory;

public interface Storable<T extends Storable> extends HasId {

    default Storable<T> save() {
        return StorageFactory.getStorage().save(this);
    }

    default Storable<T> get() {
        return StorageFactory.getStorage().get(this.getId());
    }

    default void drop() {
        StorageFactory.getStorage().drop(this.getId());
    }
}
