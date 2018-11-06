package com.java.zookeeper.curator.scene.eventListener.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 〈peizhi 〉
 *
 * @author xsh
 * @date 2018/10/27
 * @since 1.0.0
 */
public class Environment implements Serializable {

    private String  name ;


    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}