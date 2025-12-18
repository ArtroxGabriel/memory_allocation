package org.example.Commands.Impl;

import org.example.Algorithm.AlgorithmFactory;
import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Enum.AlgorithmEnum;
import org.example.Memory.AbstractMemoryManagement;

public class AllocCommand extends AbstractCommand {

    private int size;
    private AlgorithmEnum algorithmEnum;

    public AllocCommand(AbstractMemoryManagement memory) {
        super(memory);
    }

    @Override
    public CommandsResult execute(String[] args) {
        var validationResult = validateArgs(args);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        if (!memory.isInitialized()) {
            return CommandsResult.Failure("Memory is not initialized.");
        }

        var memoryResult = memory.allocateMemory(size, algorithmEnum);
        if (!memoryResult.isSuccess()) {
            return CommandsResult.Failure("Allocation failed: " + memoryResult.getMessage());
        }

        return CommandsResult.Success();
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        if (args.length != 2) {
            return CommandsResult.Failure("ALLOC command requires exactly two argument: <size> and <alg>");

        }

        try {
            long parsedSize = Long.parseLong(args[0]);
            if (parsedSize > Integer.MAX_VALUE) {
                return CommandsResult.Failure("First argument <size> is too large. Maximum allowed is " + Integer.MAX_VALUE + ".");
            }
            size = (int) parsedSize;


            algorithmEnum = AlgorithmEnum.valueOf(args[1].toUpperCase());

            return CommandsResult.Success();
        } catch (NumberFormatException e) {
            return CommandsResult.Failure("First argument <size> is not a valid integer.");
        } catch (IllegalArgumentException e) {
            return CommandsResult.Failure("Second argument <alg> must be one of the following: first_fit, best_fit, worst_fit.");
        }
    }
}
