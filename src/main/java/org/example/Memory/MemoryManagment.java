package org.example.Memory;

import lombok.Getter;

@Getter
public class MemoryManagment extends AbstractMemoryManagment {

    public MemoryManagment(int size) {
        super(size);
    }

    @Override
    public boolean freeMemory(int startIndex, int size) {
        return true;
    }

    @Override
    public boolean allocateMemory(int startIndex, int size) {
        return true;
    }
}
