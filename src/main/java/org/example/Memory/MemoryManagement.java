package org.example.Memory;

import lombok.Getter;

@Getter
public class MemoryManagement extends AbstractMemoryManagement {
    private int nextId = 0;

    @Override
    public boolean freeMemory(int id) {
        return false;
    }

    @Override
    public boolean allocateMemory(int startIndex, int size) {
        return false;
    }
}
