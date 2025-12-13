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

        return CommandsResult.Success("TODO: MUST BE IMPLEMENTED");
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        return null;
    }
}
