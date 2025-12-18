package org.example.Memory;

import lombok.Getter;
import org.example.Algorithm.AlgorithmFactory;
import org.example.Enum.AlgorithmEnum;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class MemoryManagement extends AbstractMemoryManagement {

    @Override
    public MemoryManagementResult allocateMemory(int size, AlgorithmEnum algorithmEnum) {
        ArrayList<MemoryBlock> memory = this.getMemory();
        HashMap<Integer, Integer> allocationMap = this.getAllocationMap();

        // Select memory block using the specified algorithm
        var algorithm = AlgorithmFactory.getAlgorithm(algorithmEnum);

        var result = algorithm.selectMemoryBlock(memory, size);
        if (!result.isSuccess()) {
            return MemoryManagementResult.Failure("Allocation failed: " + result.getErrorOrElseThrow().message());
        }

        int startIndex = result.getStartAddress();

        // Allocate the memory blocks
        int id = this.getNextId();
        allocationMap.put(id, startIndex);
        for (int i = startIndex; i < size + startIndex; i++) {
            memory.get(i).allocate(this.getNextId());
        }

        this.setNextId(id + 1);
        return MemoryManagementResult.Success("Process " + id + " allocated at index " + startIndex + " with size " + size);
    }

    @Override
    public MemoryManagementResult freeMemory(int id) {
        ArrayList<MemoryBlock> memory = this.getMemory();
        HashMap<Integer, Integer> allocationMap = this.getAllocationMap();

        // Free all blocks belonging to the process
        int initialIndex = allocationMap.getOrDefault(id, -1);
        if (initialIndex == -1) {
            return MemoryManagementResult.Failure("Process " + id + " not found in memory");
        }

        for (int i = initialIndex; i < memory.size(); i++) {
            MemoryBlock block = memory.get(i);
            if (block.getId() == id) {
                block.free();
            } else {
                break; // Stop when we reach a block that doesn't belong to the process
            }
        }

        allocationMap.remove(id);

        return MemoryManagementResult.Success("Process " + id + " freed from memory");
    }
}
