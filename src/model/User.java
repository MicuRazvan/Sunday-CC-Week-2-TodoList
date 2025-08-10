package model;

import java.io.Serializable;

//using serializable bcs when dragging a task that has an user attached to it, swing transform that object in a stream of bytes.
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
