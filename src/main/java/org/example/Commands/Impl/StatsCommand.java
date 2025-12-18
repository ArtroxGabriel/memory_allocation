package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Memory.AbstractMemoryManagement;

public class StatsCommand extends AbstractCommand {
    public StatsCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] _args) {
        if (!memory.isInitialized()) {
            return CommandsResult.Failure("Memory is not initialized.");
        }
        var out = new StringBuilder();
        out.append("== Statistics ==\n");
        var memory = this.memory.getMemory();

        int totalSize = memory.size();
        int occupiedSize = 0;
        int freeSize = 0;
        int holes = 0;
        boolean isCountingHole = false;

        for (var block : memory) {
            if (block.isFree()) {
                freeSize++;
                if (!isCountingHole) {
                    holes++;
                    isCountingHole = true;
                }
            } else {
                occupiedSize++;
                isCountingHole = false;
            }
        }

        out.append("Total Size: ").append(totalSize).append(" bytes\n");
        out.append("Occupied: ").append(occupiedSize).append(" bytes | Free: ").append(freeSize).append(" bytes\n");
        out.append("Holes (external fragmentation): ").append(holes).append("\n");

        double effectiveUsage = totalSize > 0 ? (double) occupiedSize / totalSize * 100 : 0;
        out.append(String.format("Effective Usage: %.2f%%\n", effectiveUsage));

        return CommandsResult.Success(out.toString());
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        throw new IllegalStateException("Stats command does not require validation.");
    }
}
