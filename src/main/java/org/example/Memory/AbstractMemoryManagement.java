package org.example.Memory;

import lombok.Getter;

import java.util.ArrayList;

/**
 * The IMemory class represents an abstract memory management system.
 * It provides methods for allocating and freeing memory blocks.
 */
public abstract class AbstractMemoryManagement {

    /**
     * Storage for memory blocks, simulating physical memory
     */
    private ArrayList<MemoryBlock> physicalMemory;
    @Getter
    private boolean isInitialized = false;

    public void initMemory(int size) {
        this.physicalMemory = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.physicalMemory.add(new MemoryBlock());
        }
        isInitialized = true;
    }

    public void resetMemory() {
        this.physicalMemory = null;
        isInitialized = false;
    }

    public ArrayList<MemoryBlock> getMemory() {
        return physicalMemory;
    }


    /**
     * Frees a block of memory starting from startIndex of given size
     *
     * @param id the id of the memory block to free
     * @return true if the memory was successfully freed, false otherwise
     */
    public abstract boolean freeMemory(int id);

    /**
     * Allocates a block of memory starting from startIndex of given size
     *
     * @param startIndex the starting index of the memory block to allocate
     * @param size       the size of the memory block to allocate
     * @return true if the memory was successfully allocated, false otherwise
     */
    public abstract boolean allocateMemory(int startIndex, int size);
}
