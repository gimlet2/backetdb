package com.restmonkeys.backetdb.server.scripting;

public class EvalFactory {

    public static final String JAVASCRIPT = "javascript";

    private EvalFactory() {

    }

    public static Eval getEval() {
        return getEval(JAVASCRIPT);
    }

    public static Eval getEval(String type) {
        if (JAVASCRIPT.equals(type)) {
            return JavascriptEval.getEval();
        }
        return null;
    }
}
