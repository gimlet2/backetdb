package com.restmonkeys.backetdb.server.storage;

public class StorageFactory {
    private StorageFactory() {

    }

    public static Storage getStorage() {
        return InMemoryStorage.getStorage();
    }
}
