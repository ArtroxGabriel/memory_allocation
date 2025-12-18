package org.example.Algorithm;

import org.example.Memory.MemoryBlock;

import java.util.ArrayList;

public interface IAlgorithmStrategy {
    /**
     * Select a memory block based on the algorithm strategy(First Fit, Best Fit, Worst Fit).
     * @param memory the list of memory blocks
     * @param id the id of the process to allocate
     * @param size the size of the process to allocate
     * @return the result {@link AlgorithmResult} of the selection
     */
    AlgorithmResult selectMemoryBlock(ArrayList<MemoryBlock> memory, int id, int size);

    /**
     * Identifies all contiguous free memory blocks.
     * @param memory the list of memory blocks
     * @return a list of lists, where each inner list contains the indices of contiguous free blocks
     */
    default ArrayList<ArrayList<Integer>> identifyFreeBlocks(ArrayList<MemoryBlock> memory) {
        ArrayList<ArrayList<Integer>> freeBlocks = new ArrayList<>();
        ArrayList<Integer> currentBlock = new ArrayList<>();

        for (int i = 0; i < memory.size(); i++) {
            MemoryBlock block = memory.get(i);
            if (block.isFree()) {
                currentBlock.add(i);
            } else if (!currentBlock.isEmpty()) {
                freeBlocks.add(new ArrayList<>(currentBlock));
                currentBlock.clear();
            }
        }

        if (!currentBlock.isEmpty()) {
            freeBlocks.add(currentBlock);
        }

        return freeBlocks;
    }

    /**
     * Finds the optimal memory block based on the specified criteria.
     * @param freeBlocks the list of contiguous free memory blocks
     * @param size the required size
     * @param findSmallest true to find the smallest suitable block (Best Fit), false for the largest (Worst Fit)
     * @return the starting index of the optimal block, or -1 if none found
     */
    default int findOptimalBlock(ArrayList<ArrayList<Integer>> freeBlocks, int size, boolean findSmallest) {
        int optimalIndex = -1;
        int optimalSize = findSmallest ? Integer.MAX_VALUE : -1;

        for (ArrayList<Integer> blockIndices : freeBlocks) {
            int blockSize = blockIndices.size();
            boolean isBetter = findSmallest
                    ? (blockSize >= size && blockSize < optimalSize)
                    : (blockSize >= size && blockSize > optimalSize);

            if (isBetter) {
                optimalSize = blockSize;
                optimalIndex = blockIndices.getFirst();
            }
        }

        return optimalIndex;
    }
}
