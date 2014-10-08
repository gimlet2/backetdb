package com.restmonkeys.backetdb.server;

import com.restmonkeys.backetdb.server.storage.StorageFactory;

public class Main {

    public static void main(String[] args) {
        new Application();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            StorageFactory.getStorage().syncToDisk();
        }));
    }
}
