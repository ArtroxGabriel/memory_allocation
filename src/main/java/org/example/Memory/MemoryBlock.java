package org.example.Memory;

import lombok.Getter;
import org.example.Enum.MemoryBlockStatus;


/**
 * The MemoryBlock class represents a block of memory with a unique identifier,
 * size, and status {@link MemoryBlockStatus}.
 */
@Getter
public class MemoryBlock {
    /**
     * the unique id of the memory block
     */
    private int id;

    /**
     * The status of the memory block
     */
    private MemoryBlockStatus status = MemoryBlockStatus.FREE;

    public void allocate(int id) {
        this.id = id;
        this.status = MemoryBlockStatus.ALLOCATED;
    }


    public void free() {
        this.id = -1;
        this.status = MemoryBlockStatus.FREE;
    }


    public boolean isFree() {
        return this.status == MemoryBlockStatus.FREE;
    }
}
