package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Memory.AbstractMemoryManagement;

public class ResetCommand extends AbstractCommand {
    public ResetCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] args) {
        if (!memory.isInitialized()) {
            return CommandsResult.Failure("Memory is not initialized.");
        }

        memory.resetMemory();

        return CommandsResult.Success("Memory has been reset.");
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        throw new IllegalStateException("Reset command does not require validation.");
    }
}
