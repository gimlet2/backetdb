package com.restmonkeys.backetdb.server.scripting;

import com.restmonkeys.backetdb.server.exceptions.OperationForbidden;
import com.restmonkeys.backetdb.server.model.Function;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavascriptEval implements Eval {

    private final static JavascriptEval eval = new JavascriptEval();

    private JavascriptEval() {
    }

    public static Eval getEval() {
        return eval;
    }

    @Override
    public <T> T eval(Function f, Object... args) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(f.getCode());
            Invocable invocable = (Invocable) engine;
            //noinspection unchecked
            return (T) invocable.invokeFunction(f.getName(), args);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new OperationForbidden();
        }
    }

}
