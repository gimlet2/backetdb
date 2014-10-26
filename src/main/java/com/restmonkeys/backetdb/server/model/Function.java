package com.restmonkeys.backetdb.server.model;

import java.io.Serializable;

public class Function implements Serializable {

    private static final long serialVersionUID = -4515481883303584209L;
    private String name;
    private String type = "javascript";
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
