package org.example.Parser.Validator;

import org.example.Enum.CommandsEnum;

import java.util.Set;

public class CommandValidator implements ICommandValidator {
    private static final Set<String> VALID_STRATEGIES = Set.of("first", "best", "worst");

    @Override
    public ValidationResult validate(CommandsEnum command, String[] args) {
        return switch (command) {
            case INIT -> validateInit(args);
            case ALLOC -> validateAlloc(args);
            case FREE_ID -> validateFreeId(args);
            case SHOW, STATS, RESET, HELP, DO_NOTHING, EXIT -> validateNoArgs(args, command);
            case UNKNOWN -> ValidationResult.failure("Unknown command");
        };
    }

    private ValidationResult validateInit(String[] args) {
        if (args.length != 1) {
            return ValidationResult.failure("INIT requires exactly 1 argument: <size>");
        }
        try {
            int size = Integer.parseInt(args[0]);
            if (size <= 0) {
                return ValidationResult.failure("Size must be positive");
            }
            return ValidationResult.success();
        } catch (NumberFormatException e) {
            return ValidationResult.failure("Size must be a valid integer");
        }
    }

    private ValidationResult validateAlloc(String[] args) {
        if (args.length != 2) {
            return ValidationResult.failure("ALLOC requires exactly 2 arguments: <size> <strategy>");
        }
        try {
            int size = Integer.parseInt(args[0]);
            if (size <= 0) {
                return ValidationResult.failure("Size must be positive");
            }
        } catch (NumberFormatException e) {
            return ValidationResult.failure("Size must be a valid integer");
        }

        String strategy = args[1].toLowerCase();
        if (!VALID_STRATEGIES.contains(strategy)) {
            return ValidationResult.failure("Invalid strategy. Use: first, best, or worst");
        }

        return ValidationResult.success();
    }

    private ValidationResult validateFreeId(String[] args) {
        if (args.length != 1) {
            return ValidationResult.failure("FREE_ID requires exactly 1 argument: <id>");
        }
        try {
            int id = Integer.parseInt(args[0]);
            if (id < 0) {
                return ValidationResult.failure("ID must be non-negative");
            }
            return ValidationResult.success();
        } catch (NumberFormatException e) {
            return ValidationResult.failure("ID must be a valid integer");
        }
    }

    private ValidationResult validateNoArgs(String[] args, CommandsEnum command) {
        if (args.length != 0) {
            return ValidationResult.failure(command.name() + " does not accept arguments");
        }
        return ValidationResult.success();
    }
}