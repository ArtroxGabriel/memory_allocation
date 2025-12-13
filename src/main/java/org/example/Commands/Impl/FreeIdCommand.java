package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Memory.AbstractMemoryManagement;

public class FreeIdCommand extends AbstractCommand {
    private int id;

    public FreeIdCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] args) {
        var validation = validateArgs(args);
        if (!validation.isSuccess()) {
            return validation;
        }

        if (!memory.isInitialized()) {
            return CommandsResult.Failure("Memory is not initialized.");
        }

        var ok = memory.freeMemory(id);
        if (!ok) {
            return CommandsResult.Failure("Failed to free memory with id " + id + ".");
        }

        return CommandsResult.Success();
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        if (args.length != 1) {
            return CommandsResult.Failure("FREE_ID command requires exactly one argument: <id>");
        }

        if (!args[0].matches("\\d+")) {
            return CommandsResult.Failure("Argument <id> must be a non-negative integer.");
        }
        id = Integer.parseInt(args[0]);

        return CommandsResult.Success();
    }
}
