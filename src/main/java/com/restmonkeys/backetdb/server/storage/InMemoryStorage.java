package com.restmonkeys.backetdb.server.storage;

import com.restmonkeys.backetdb.server.exceptions.OperationForbidden;
import com.restmonkeys.backetdb.server.model.Storable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStorage implements Storage {

    private static final Storage storage = new InMemoryStorage();
    private static AtomicLong counter;
    private static Map<Long, Storable<? extends Storable>> map;

    private InMemoryStorage() {
        try {
            FileInputStream fos = new FileInputStream("map.ser");
            ObjectInputStream oos = new ObjectInputStream(fos);
            //noinspection unchecked
            map = Collections.synchronizedMap((Map<Long, Storable<? extends Storable>>) oos.readObject());
            counter = new AtomicLong(map.keySet().stream().max(Comparator.<Long>naturalOrder()).get());
            oos.close();
        } catch (Exception e) {
            map = Collections.synchronizedMap(new HashMap<>());
            counter = new AtomicLong(1);
            e.printStackTrace();
        }
    }

    public static Storage getStorage() {
        return storage;
    }

    @Override
    public <T extends Storable> Storable<T> save(Storable<T> object) {
        long key = object.getId().orElse(counter.incrementAndGet());
        if (!map.containsKey(key)) {
            map.put(key, object);
            object.setId(key);
        } else {
            map.replace(key, object);
        }
        return object;
    }

    @Override
    public <T extends Storable> Storable<T> get(Optional<Long> id) {
        //noinspection unchecked
        return (Storable<T>) map.get(id.orElseThrow(OperationForbidden::new));
    }

    @Override
    public void drop(Optional<Long> id) {
        if (id.isPresent()) {
            map.remove(id.get());
        }
    }

    @Override
    public Set<Long> ids() {
        return map.keySet();
    }

    @Override
    public void syncToDisk() {
        try {
            FileOutputStream fos = new FileOutputStream("map.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
