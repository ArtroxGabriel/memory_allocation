package org.example.Parser;

import org.example.Enum.CommandsEnum;

public class CommandParser implements ICommandParser {
    @Override
    public CommandParserResult parse(String input) {
        var tokens = input.trim().split("\\s+");


        if (tokens.length == 0 || tokens[0].isEmpty()) {
            return CommandParserResult.Success(CommandsEnum.DO_NOTHING);
        }

        String commandStr = tokens[0];
        var command = mapToEnum(commandStr);
        if (command == CommandsEnum.UNKNOWN) {
            return CommandParserResult.Failure("Unknown command: " + commandStr);
        }

        var args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, tokens.length - 1);
        return CommandParserResult.Success(command, args);
    }

    public CommandsEnum mapToEnum(String command) {
        try {
            return CommandsEnum.valueOf(command.toUpperCase());
        } catch (Exception ex) {
            return CommandsEnum.UNKNOWN;
        }
    }
}
