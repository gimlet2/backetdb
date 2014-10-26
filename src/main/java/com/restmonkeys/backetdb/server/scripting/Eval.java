package com.restmonkeys.backetdb.server.scripting;

import com.restmonkeys.backetdb.server.model.Function;

public interface Eval {

    <T> T eval(Function f, Object... args);
}
