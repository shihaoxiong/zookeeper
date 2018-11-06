package com.java.zookeeper.api.domain;

/**
 * 〈user 〉
 *
 * @author xsh
 * @date 2018/10/26
 * @since 1.0.0
 */
public class User {

    private long id;

    private String name ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}