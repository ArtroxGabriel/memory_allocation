package org.example.Commands;

import org.example.Commands.Impl.AllocCommand;
import org.example.Commands.Impl.FreeIdCommand;
import org.example.Memory.MemoryBlock;
import org.example.Memory.MemoryManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandsIntegrationTest {

    private MemoryManagement memory;
    private AllocCommand allocCommand;
    private FreeIdCommand freeIdCommand;

    @BeforeEach
    void setUp() {
        memory = new MemoryManagement();
        memory.initMemory(20); // Initialize with 20 blocks
        allocCommand = new AllocCommand(memory);
        freeIdCommand = new FreeIdCommand(memory);
    }

    @Test
    @DisplayName("Should allocate and free memory successfully")
    void allocateAndFree_Success() {
        // ALLOCATE
        String[] allocArgs = {"5", "FIRST_FIT"};
        CommandsResult allocResult = allocCommand.execute(allocArgs);

        assertThat(allocResult.isSuccess()).isTrue();

        // FREE
        String[] freeArgs = {"1"};
        CommandsResult freeResult = freeIdCommand.execute(freeArgs);

        assertThat(freeResult.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("Should allocate multiple processes and free them")
    void allocateMultipleProcesses_FreeAll_Success() {
        // ALLOCATE Process 1: 5 blocks
        allocCommand.execute(new String[]{"5", "FIRST_FIT"});

        // ALLOCATE Process 2: 3 blocks
        allocCommand.execute(new String[]{"3", "BEST_FIT"});

        // ALLOCATE Process 3: 7 blocks
        allocCommand.execute(new String[]{"7", "WORST_FIT"});

        // Memory should have fewer free blocks
        long freeBlocks = memory.getMemory().stream()
                .filter(MemoryBlock::isFree)
                .count();
        assertThat(freeBlocks).isEqualTo(5);

        // FREE Process 2
        CommandsResult freeResult = freeIdCommand.execute(new String[]{"2"});
        assertThat(freeResult.isSuccess()).isTrue();

        // FREE Process 1
        freeResult = freeIdCommand.execute(new String[]{"1"});
        assertThat(freeResult.isSuccess()).isTrue();

        // FREE Process 3
        freeResult = freeIdCommand.execute(new String[]{"3"});
        assertThat(freeResult.isSuccess()).isTrue();

        // After freeing all, memory should be consolidated
        assertThat(memory.getMemory().getFirst().isFree()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"FIRST_FIT", "BEST_FIT", "WORST_FIT"})
    @DisplayName("Should allocate with different algorithms and free successfully")
    void allocateWithDifferentAlgorithms_Free_Success(String algorithm) {
        // ALLOCATE
        String[] allocArgs = {"8", algorithm};
        CommandsResult allocResult = allocCommand.execute(allocArgs);
        assertThat(allocResult.isSuccess()).isTrue();

        // Verify allocation
        long allocatedBlocks = memory.getMemory().stream()
                .filter(block -> !block.isFree() && block.getId() == 1)
                .count();
        assertThat(allocatedBlocks).isEqualTo(8);

        // FREE
        String[] freeArgs = {"1"};
        CommandsResult freeResult = freeIdCommand.execute(freeArgs);
        assertThat(freeResult.isSuccess()).isTrue();

        // Verify all blocks are free
        long freeBlocks = memory.getMemory().stream()
                .filter(MemoryBlock::isFree)
                .count();
        assertThat(freeBlocks).isEqualTo(memory.getMemory().size());
    }

    @Test
    @DisplayName("Should handle coalescence after freeing middle process")
    void allocateThreeProcesses_FreeMiddle_Coalescence() {
        // ALLOCATE P1: 3 blocks
        allocCommand.execute(new String[]{"3", "FIRST_FIT"});

        // ALLOCATE P2: 4 blocks
        allocCommand.execute(new String[]{"4", "FIRST_FIT"});

        // ALLOCATE P3: 5 blocks
        allocCommand.execute(new String[]{"5", "FIRST_FIT"});

        // Memory structure: [P1 P1 P1 P2 P2 P2 P2 P3 P3 P3 P3 P3 FREE...]
        assertThat(memory.getMemory().size()).isGreaterThanOrEqualTo(12);

        // FREE P2 (middle process)
        freeIdCommand.execute(new String[]{"2"});

        // Memory structure should now be: [P1 P1 P1 FREE P3 P3 P3 P3 P3 FREE...]
        // with coalescence of adjacent free blocks
        long freeBlocksAfterP2 = memory.getMemory().stream()
                .filter(MemoryBlock::isFree)
                .count();

        // FREE P1
        freeIdCommand.execute(new String[]{"1"});

        // Should coalesce P1 with the free block after it
        long freeBlocksAfterP1 = memory.getMemory().stream()
                .filter(MemoryBlock::isFree)
                .count();
        assertThat(freeBlocksAfterP1).isGreaterThan(freeBlocksAfterP2);

        // FREE P3
        freeIdCommand.execute(new String[]{"3"});

        assertThat(memory.getMemory().getFirst().isFree()).isTrue();
    }

    @Test
    @DisplayName("Should fail to allocate when memory is full")
    void allocateLargeProcess_MemoryFull_Failure() {
        // ALLOCATE Process that fills entire memory
        allocCommand.execute(new String[]{"20", "FIRST_FIT"});

        // Try to allocate another process
        String[] args = {"5", "FIRST_FIT"};
        CommandsResult result = allocCommand.execute(args);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("Allocation failed");
    }

    @Test
    @DisplayName("Should fail to free non-existent process")
    void freeNonExistentProcess_Failure() {
        // Try to free process that was never allocated
        String[] freeArgs = {"999"};
        CommandsResult result = freeIdCommand.execute(freeArgs);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("Freeing memory failed");
    }

    @Test
    @DisplayName("Should allocate after freeing and reuse freed space")
    void allocateFreeReallocate_ReuseSpace() {
        // ALLOCATE P1: 10 blocks
        allocCommand.execute(new String[]{"10", "FIRST_FIT"});

        // FREE P1
        freeIdCommand.execute(new String[]{"1"});

        // ALLOCATE P2: 5 blocks (should reuse freed space)
        CommandsResult allocResult = allocCommand.execute(new String[]{"5", "FIRST_FIT"});
        assertThat(allocResult.isSuccess()).isTrue();

        // Verify P2 uses the freed space
        long p2Blocks = memory.getMemory().stream()
                .filter(block -> !block.isFree() && block.getId() == 2)
                .count();
        assertThat(p2Blocks).isEqualTo(5);

        // Should still have free blocks
        long freeBlocks = memory.getMemory().stream()
                .filter(MemoryBlock::isFree)
                .count();
        assertThat(freeBlocks).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle fragmentation and allocation with best fit")
    void fragmentedMemory_BestFit_FindsOptimalBlock() {
        // Create fragmentation
        allocCommand.execute(new String[]{"3", "FIRST_FIT"}); // P1
        allocCommand.execute(new String[]{"5", "FIRST_FIT"}); // P2
        allocCommand.execute(new String[]{"2", "FIRST_FIT"}); // P3
        allocCommand.execute(new String[]{"4", "FIRST_FIT"}); // P4

        // Free P2 (5 blocks) and P4 (4 blocks)
        freeIdCommand.execute(new String[]{"2"});
        freeIdCommand.execute(new String[]{"4"});

        // Memory has gaps of 5 and 4 blocks
        // Allocate 4 blocks with BEST_FIT (should use exact fit)
        CommandsResult result = allocCommand.execute(new String[]{"4", "BEST_FIT"});
        assertThat(result.isSuccess()).isTrue();

        // Verify allocation succeeded
        long p5Blocks = memory.getMemory().stream()
                .filter(block -> !block.isFree() && block.getId() == 5)
                .count();
        assertThat(p5Blocks).isEqualTo(4);
    }

    @Test
    @DisplayName("Should handle worst fit allocation strategy")
    void fragmentedMemory_WorstFit_FindsLargestBlock() {
        // Create fragmentation with different sized gaps
        allocCommand.execute(new String[]{"2", "FIRST_FIT"}); // P1
        allocCommand.execute(new String[]{"8", "FIRST_FIT"}); // P2
        allocCommand.execute(new String[]{"3", "FIRST_FIT"}); // P3

        // Free P2 (8 blocks) - creates large gap
        freeIdCommand.execute(new String[]{"2"});

        // Allocate 3 blocks with WORST_FIT (should use largest available block)
        CommandsResult result = allocCommand.execute(new String[]{"3", "WORST_FIT"});
        assertThat(result.isSuccess()).isTrue();

        // Verify allocation
        long p4Blocks = memory.getMemory().stream()
                .filter(block -> !block.isFree() && block.getId() == 4)
                .count();
        assertThat(p4Blocks).isEqualTo(3);
    }

    @Test
    @DisplayName("Should fail to allocate before memory initialization")
    void allocateBeforeInit_Failure() {
        // Create new commands with uninitialized memory
        MemoryManagement uninitMemory = new MemoryManagement();
        AllocCommand uninitAllocCmd = new AllocCommand(uninitMemory);

        String[] args = {"5", "FIRST_FIT"};
        CommandsResult result = uninitAllocCmd.execute(args);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Memory is not initialized.");
    }

    @Test
    @DisplayName("Should fail to free before memory initialization")
    void freeBeforeInit_Failure() {
        // Create new commands with uninitialized memory
        MemoryManagement uninitMemory = new MemoryManagement();
        FreeIdCommand uninitFreeCmd = new FreeIdCommand(uninitMemory);

        String[] args = {"1"};
        CommandsResult result = uninitFreeCmd.execute(args);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Memory is not initialized.");
    }
}