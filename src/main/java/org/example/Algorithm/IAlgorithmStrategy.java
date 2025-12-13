package org.example.Algorithm;

import org.example.Memory.MemoryBlock;

import java.util.ArrayList;

public interface IAlgorithmStrategy {
    /**
     * Select a memory block based on the algorithm strategy(First Fit, Best Fit, Worst Fit).
     * @param memory the list of memory blocks
     * @param size the size of the process to allocate
     * @return the result {@link AlgorithmResult} of the selection
     */
    AlgorithmResult selectMemoryBlock(ArrayList<MemoryBlock> memory, int size);
}
