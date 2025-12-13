package org.example.Memory;

import lombok.Getter;

@Getter
public class MemoryManagement extends AbstractMemoryManagement {

    @Override
    public boolean freeMemory(int startIndex, int size) {
        return true;
    }

    @Override
    public boolean allocateMemory(int startIndex, int size) {
        return true;
    }
}
