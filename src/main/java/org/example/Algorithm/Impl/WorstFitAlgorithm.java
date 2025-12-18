package org.example.Algorithm.Impl;

import org.example.Algorithm.AlgorithmResult;
import org.example.Algorithm.IAlgorithmStrategy;
import org.example.Memory.MemoryBlock;

import java.util.ArrayList;

public class WorstFitAlgorithm implements IAlgorithmStrategy {
    @Override
    public AlgorithmResult selectMemoryBlock(ArrayList<MemoryBlock> memory, int id, int size) {
        ArrayList<ArrayList<Integer>> freeBlocks = identifyFreeBlocks(memory);
        if (freeBlocks.isEmpty()) {
            return AlgorithmResult.Failure("Memory is full");
        }

        int worstIndex = findOptimalBlock(freeBlocks, size, false);

        if (worstIndex == -1) {
            return AlgorithmResult.Failure("No suitable memory block found for size " + size);
        }

        return AlgorithmResult.Success(worstIndex, id);
    }
}