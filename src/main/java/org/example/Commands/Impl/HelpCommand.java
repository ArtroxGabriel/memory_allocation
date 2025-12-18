package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Enum.CommandsEnum;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super(null);
    }

    @Override
    public CommandsResult execute(String[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append("Available commands:\n");
        for (CommandsEnum command : CommandsEnum.values()) {
            switch (command) {
                case INIT:
                    builder.append("- init <memory_size>").append("\t - Initialize memory with the specified total size.\n");
                    break;
                case ALLOC:
                    builder.append("- alloc <size> <alg>").append("\t - Allocate a block of memory of the specified size using the specified algorithm (first, best, worst).\n");
                    break;
                case FREEID:
                    builder.append("- freeid <id>").append("\t\t - Free the memory block with the specified ID.\n");
                    break;
                case SHOW:
                    builder.append("- show").append("\t\t\t - Display the current state of memory blocks.\n");
                    break;
                case STATS:
                    builder.append("- stats").append("\t\t\t - Show memory usage statistics.\n");
                    break;
                case RESET:
                    builder.append("- reset").append("\t\t\t - Reset the memory to its initial state.\n");
                    break;
                case HELP:
                    builder.append("- help").append("\t\t\t - Display this help message.\n");
                    break;
                case EXIT:
                    builder.append("- exit").append("\t\t\t - Exit the memory management shell.\n");
                    break;
                default:
                    break;
            }
        }

        return CommandsResult.Success(builder.toString());
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        throw new IllegalStateException("Help command does not require validation.");
    }
}
