package org.example.Commands;

import lombok.Getter;

@Getter
public class CommandsResult {
    private final String message;
    private final boolean isSuccess;

    private CommandsResult(String message, boolean success) {
        this.isSuccess = success;
        this.message = message;
    }


    public static CommandsResult Success() {
        return new CommandsResult("", true);
    }

    public static CommandsResult Success(String message) {
        return new CommandsResult(message, true);
    }

    public static CommandsResult Failure(String errorMessage) {
        return new CommandsResult(errorMessage, false);
    }

}
