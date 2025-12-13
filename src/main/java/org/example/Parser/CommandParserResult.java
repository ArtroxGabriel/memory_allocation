package org.example.Parser;

import lombok.Getter;
import org.example.Enum.CommandsEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
public class CommandParserResult {
    private CommandsEnum command;
    private String[] args;

    private ParserError error;


    private CommandParserResult(CommandsEnum command, String[] args) {
        this.command = command;
        this.args = args;
    }

    private CommandParserResult(ParserError error) {
        this.error = error;
    }

    public static CommandParserResult Success(CommandsEnum command, String[] args) {
        return new CommandParserResult(command, args);
    }

    public static CommandParserResult Success(CommandsEnum command) {

        return new CommandParserResult(command, new String[]{});
    }

    public static CommandParserResult Failure(String message) {
        return new CommandParserResult(new ParserError(message));
    }


    public boolean isSuccess() {
        return error == null;
    }

    public SuccessData getResultOrElseThrow() throws IllegalStateException {
        if (!isSuccess()) {
            throw new IllegalStateException("Cannot get result data from a failed parse.");
        }
        return new SuccessData(command, args);
    }

    public ParserError getErrorOrElseThrow() throws IllegalStateException {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot get error data from a successful parse.");
        }

        return error;
    }

    public record SuccessData(CommandsEnum command, String[] args) {
        @Override
        public @NotNull String toString() {
            return "Command: " + command + ", Args: " + Arrays.toString(args);
        }
    }

    public record ParserError(String message) {
    }
}
