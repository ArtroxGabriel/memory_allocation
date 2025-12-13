package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Memory.AbstractMemoryManagement;

public class InitCommand extends AbstractCommand {

    private int totalMemorySize;

    public InitCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] args) {
        var validationResult = validateArgs(args);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        if (memory.isInitialized()) {
            return CommandsResult.Failure("Memory has already been initialized.");
        }

        memory.initMemory(totalMemorySize);

        return CommandsResult.Success();
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        if (args.length != 1) {
            return CommandsResult.Failure("Init command requires exactly one argument: <total_memory_size>");
        }

        if (!args[0].matches("\\d+")) {
            return CommandsResult.Failure("Argument <size> must be a non-negative integer.");
        }
        totalMemorySize = Integer.parseInt(args[0]);

        if (totalMemorySize <= 0) {
            return CommandsResult.Failure("Total memory size must be greater than zero.");
        }


        return CommandsResult.Success();
    }
}