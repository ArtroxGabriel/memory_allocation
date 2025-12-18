package org.example.Algorithm.Impl;

import org.example.Algorithm.AlgorithmResult;
import org.example.Algorithm.IAlgorithmStrategy;
import org.example.Memory.MemoryBlock;

import java.util.ArrayList;

public class FirstFitAlgorithm implements IAlgorithmStrategy {
    @Override
    public AlgorithmResult selectMemoryBlock(ArrayList<MemoryBlock> memory, int id, int size) {
        ArrayList<Integer> possibleAllocations = new ArrayList<>();

        for (int i = 0; i < memory.size(); i++) {
            MemoryBlock block = memory.get(i);
            if (block.isFree()) {
                possibleAllocations.add(i);
                if (possibleAllocations.size() == size) {
                    return AlgorithmResult.Success(i, id);
                }
            } else {
                possibleAllocations.clear();
            }
        }

        return AlgorithmResult.Failure("No sufficient contiguous memory block found for size " + size);
    }
}
