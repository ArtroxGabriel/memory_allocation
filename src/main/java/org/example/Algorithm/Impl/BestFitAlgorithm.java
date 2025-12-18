package org.example.Algorithm.Impl;

import org.example.Algorithm.AlgorithmResult;
import org.example.Algorithm.IAlgorithmStrategy;
import org.example.Memory.MemoryBlock;

import java.util.ArrayList;

public class BestFitAlgorithm implements IAlgorithmStrategy {
    @Override
    public AlgorithmResult selectMemoryBlock(ArrayList<MemoryBlock> memory, int id, int size) {
        ArrayList<ArrayList<Integer>> freeBlocks = identifyFreeBlocks(memory);
        if (freeBlocks.isEmpty()) {
            return AlgorithmResult.Failure("Memory is full");
        }

        int bestIndex = findOptimalBlock(freeBlocks, size, true);

        if (bestIndex == -1) {
            return AlgorithmResult.Failure("No suitable memory block found for size " + size);
        }

        return AlgorithmResult.Success(bestIndex, id);
    }
}