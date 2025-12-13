package org.example.Commands;


import org.example.Memory.AbstractMemoryManagment;

public abstract class Commands {

    protected final AbstractMemoryManagment memory;

    public Commands(AbstractMemoryManagment memory) {
        this.memory = memory;
    }

    abstract public void execute(String[] args);
}
