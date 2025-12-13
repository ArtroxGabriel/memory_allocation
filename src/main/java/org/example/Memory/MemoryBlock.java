package org.example.Memory;

import lombok.Getter;
import org.example.Enum.MemoryBlockStatus;


/**
 * The MemoryBlock class represents a block of memory with a unique identifier,
 * size, and status {@link MemoryBlockStatus}.
 */
@Getter
public class MemoryBlock {
    static long nextId = 0;
    /**
     * the unique id of the memory block
     */
    private final long id = nextId++;

    /**
     * The status of the memory block
     */
    private MemoryBlockStatus status = MemoryBlockStatus.FREE;

    public void allocate(long processId) {
        this.status = MemoryBlockStatus.ALLOCATED;
    }


    public void free() {
        this.status = MemoryBlockStatus.FREE;
    }


    public boolean isFree() {
        return this.status == MemoryBlockStatus.FREE;
    }
}
