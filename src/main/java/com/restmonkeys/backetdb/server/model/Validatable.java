package com.restmonkeys.backetdb.server.model;

import javax.xml.bind.ValidationException;

public interface Validatable extends Storable {
    default Storable validate() throws ValidationException {
        return this;
    }
}
