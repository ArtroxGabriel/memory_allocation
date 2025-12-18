package org.example.Algorithm.Impl;

import org.example.Algorithm.AlgorithmResult;
import org.example.Algorithm.IAlgorithmStrategy;
import org.example.Memory.MemoryBlock;

import java.util.ArrayList;

public class FirstFitAlgorithm implements IAlgorithmStrategy {
    @Override
    public AlgorithmResult selectMemoryBlock(ArrayList<MemoryBlock> memory, int id, int size) {
        int consecutiveFree = 0;
        int startIndex = -1;

        for (int i = 0; i < memory.size(); i++) {
            MemoryBlock block = memory.get(i);
            if (block.isFree()) {
                if (consecutiveFree == 0) {
                    startIndex = i;
                }
                consecutiveFree++;
                if (consecutiveFree == size) {
                    return AlgorithmResult.Success(startIndex, id);
                }
            } else {
                consecutiveFree = 0;
                startIndex = -1;
            }
        }

        return AlgorithmResult.Failure("No sufficient contiguous memory block found for size " + size);
    }
}
