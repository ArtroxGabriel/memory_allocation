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

        var memoryResult = memory.freeMemory(id);
        if (!memoryResult.isSuccess()) {
            return CommandsResult.Failure("Freeing memory failed: " + memoryResult.getMessage());
        }

        return CommandsResult.Success();
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        if (args.length != 1) {
            return CommandsResult.Failure("FREE_ID command requires exactly one argument: <id>");
        }

        try {
            long parsedId = Long.parseLong(args[0]);
            if (parsedId > Integer.MAX_VALUE) {
                return CommandsResult.Failure("First argument <id> is too large. Maximum allowed is " + Integer.MAX_VALUE + ".");
            }
            id = (int) parsedId;

            return CommandsResult.Success();
        } catch (NumberFormatException e) {
            return CommandsResult.Failure("First argument <id> is not a valid integer.");
        }
    }
}
