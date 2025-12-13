package org.example.Memory;

import java.util.ArrayList;

/**
 * The IMemory class represents an abstract memory management system.
 * It provides methods for allocating and freeing memory blocks.
 */
public abstract class AbstractMemoryManagment {

    /**
     * Storage for memory blocks, simulating physical memory
     */
    private final ArrayList<MemoryBlock> physicalMemory;

    public AbstractMemoryManagment(int size) {
        this.physicalMemory = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.physicalMemory.add(new MemoryBlock());
        }
    }

    public ArrayList<MemoryBlock> getMemory() {
        return physicalMemory;
    }


    /**
     * Frees a block of memory starting from startIndex of given size
     *
     * @param startIndex the starting index of the memory block to free
     * @param size       the size of the memory block to free
     * @return true if the memory was successfully freed, false otherwise
     */
    abstract boolean freeMemory(int startIndex, int size);

    /**
     * Allocates a block of memory starting from startIndex of given size
     *
     * @param startIndex the starting index of the memory block to allocate
     * @param size       the size of the memory block to allocate
     * @return true if the memory was successfully allocated, false otherwise
     */
    abstract boolean allocateMemory(int startIndex, int size);
}
