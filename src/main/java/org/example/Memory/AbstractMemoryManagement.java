package org.example.Memory;

import lombok.Getter;
import lombok.Setter;
import org.example.Enum.AlgorithmEnum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The IMemory class represents an abstract memory management system.
 * It provides methods for allocating and freeing memory blocks.
 */
public abstract class AbstractMemoryManagement {
    @Getter
    @Setter
    private int nextId = 1;

    /**
     * Storage for memory blocks, simulating physical memory
     */
    private ArrayList<MemoryBlock> physicalMemory;

    /**
     * Storage for allocation mapping: process ID -> starting index
     */
    @Getter
    private HashMap<Integer, Integer> allocationMap = new HashMap<>();

    @Getter
    private boolean isInitialized = false;

    public boolean isFree(int index) {
        return physicalMemory.get(index).isFree();
    }

    public void initMemory(int size) {
        this.physicalMemory = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.physicalMemory.add(new MemoryBlock());
        }
        isInitialized = true;
    }

    public void resetMemory() {
        this.physicalMemory = null;
        this.nextId = 0;
        isInitialized = false;
    }

    public ArrayList<MemoryBlock> getMemory() {
        return physicalMemory;
    }

    /**
     * Frees a block of memory starting from startIndex of given size
     *
     * @param id the id of the memory block to free
     * @return result of the memory freeing operation
     */
    public abstract MemoryManagementResult freeMemory(int id);

    /**
     * Allocates a block of memory starting from startIndex of given size
     *
     * @param size      the size of the memory block to allocate
     * @param algorithm the algorithm to use for memory allocation
     * @return result of the memory allocation operation
     */
    public abstract MemoryManagementResult allocateMemory(int size, AlgorithmEnum algorithm);
}
