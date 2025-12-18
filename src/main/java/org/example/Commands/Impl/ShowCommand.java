package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Memory.AbstractMemoryManagement;

public class ShowCommand extends AbstractCommand {
    public ShowCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] args) {
        if (!memory.isInitialized()) {
            return CommandsResult.Failure("Memory is not initialized.");
        }
        var out = new StringBuilder();
        var memorySize = memory.getMemory().size();
        out.append("Memory Map (").append(memorySize).append(" bytes )\n");

        var sepLine = "-".repeat(Math.max(0, memorySize + 2));
        out.append(sepLine);
        out.append("\n[");
        for (int i = 0; i < memorySize; i++) {
            if (memory.isFree(i)) {
                out.append(".");
            } else {
                out.append("#");
            }
        }
        out.append("]\n");

        out.append("[");
        for (int i = 0; i < memorySize; i++) {
            if (memory.isFree(i)) {
                out.append(".");
            } else {
                out.append(memory.getMemory().get(i).getId());
            }
        }
        out.append("]\n");
        out.append(sepLine);

        return CommandsResult.Success(out.toString());
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        throw new IllegalStateException("Show command does not require validation.");
    }
}
