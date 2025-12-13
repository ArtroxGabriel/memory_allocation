package org.example.Commands;

import org.example.Memory.AbstractMemoryManagement;

public class InitCommand extends AbstractCommand {

    public InitCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] args) {
        var validationResult = validate(args);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        if (memory.isInitialized()) {
            return CommandsResult.Failure("Memory has already been initialized.");
        }

        // the convert is safe here because of the validation step
        int totalMemorySize = Integer.parseInt(args[0]);
        memory.initMemory(totalMemorySize);

        return CommandsResult.Success();
    }

    @Override
    public CommandsResult validate(String[] args) {
        if (args.length != 1) {
            return CommandsResult.Failure("Init command requires exactly one argument: <total_memory_size>");
        }

        try {
            int totalMemorySize = Integer.parseInt(args[0]);
            if (totalMemorySize <= 0) {
                return CommandsResult.Failure("Total memory size must be a positive integer.");
            }
            return CommandsResult.Success("Init command validated successfully.");
        } catch (NumberFormatException e) {
            return CommandsResult.Failure("Total memory size must be a valid integer.");
        }
    }
}