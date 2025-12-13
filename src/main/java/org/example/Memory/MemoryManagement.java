package org.example.Memory;

import lombok.Getter;

@Getter
public class MemoryManagement extends AbstractMemoryManagement {

    @Override
    public MemoryManagementResult freeMemory(int id) {
        return MemoryManagementResult.Failure("not implemented");
    }

    @Override
    public MemoryManagementResult allocateMemory(int startIndex, int size) {
        return MemoryManagementResult.Failure("not implemented");
    }
}
