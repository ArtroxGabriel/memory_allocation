package org.example.Memory;

import lombok.Getter;
import org.example.Algorithm.AlgorithmFactory;
import org.example.Enum.AlgorithmEnum;

import java.util.ArrayList;

@Getter
public class MemoryManagement extends AbstractMemoryManagement {

    @Override
    public MemoryManagementResult freeMemory(int id) {
        return MemoryManagementResult.Failure("not implemented");
    }

    @Override
    public MemoryManagementResult allocateMemory(int size, AlgorithmEnum algorithmEnum) {
        ArrayList<MemoryBlock> memory = this.getMemory();

        // Select memory block using the specified algorithm
        var algorithm = AlgorithmFactory.getAlgorithm(algorithmEnum);

        var result = algorithm.selectMemoryBlock(memory, size);
        if (!result.isSuccess()) {
            return MemoryManagementResult.Failure("Allocation failed: " + result.getErrorOrElseThrow().message());
        }

        int startIndex = result.getStartAddress();

        // Allocate the memory blocks
        int id = this.getNextId();
        for (int i = startIndex; i < size; i++) {
            memory.get(i).allocate(this.getNextId());
        }

        this.setNextId(id + 1);
        return MemoryManagementResult.Success("Process " + id + " allocated at index " + startIndex + " with size " + size);
    }
}
