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

        var algorithm = AlgorithmFactory.getAlgorithm(algorithmEnum);

        var result = algorithm.selectMemoryBlock(memory.getMemory(), size);
        if (!result.isSuccess()) {
            return CommandsResult.Failure("Allocation failed: " + result.getErrorOrElseThrow().message());
        }

        var algorithmResult = result.getResultOrElseThrow();

        boolean ok = memory.allocateMemory(algorithmResult.startAddress(), size);
        if (!ok) {
            return CommandsResult.Failure("Allocation failed: Unable to allocate memory block.");
        }

        return CommandsResult.Success();
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        if (args.length != 2) {
            return CommandsResult.Failure("ALLOC command requires exactly two argument: <size> and <alg>");

        }

        if (!args[0].matches("\\d+")) {
            return CommandsResult.Failure("First argument <size> must be a non-negative integer.");
        }
        size = Integer.parseInt(args[0]);

        try {
            algorithmEnum = AlgorithmEnum.valueOf(args[1].toUpperCase());

            return CommandsResult.Success();
        } catch (IllegalArgumentException e) {
            return CommandsResult.Failure("Second argument <alg> must be one of the following: first_fit, best_fit, worst_fit.");
        }
    }
}
