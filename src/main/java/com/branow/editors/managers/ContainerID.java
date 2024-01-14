package com.branow.editors.managers;

public class ContainerID<I> {

    private final I id;

    public ContainerID(I id) {
        this.id = id;
    }

    public I getId() {
        return id;
    }

}
