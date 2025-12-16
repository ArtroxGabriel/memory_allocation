package org.example.shell;

import org.example.Commands.AbstractCommand;
import org.example.Commands.Impl.NoneCommand;
import org.example.Enum.CommandsEnum;
import org.example.Parser.ICommandParser;

import java.util.Map;
import java.util.Scanner;

public class Shell implements AutoCloseable {
    private final Scanner scanner;
    private final ICommandParser parser;
    private final Map<CommandsEnum, AbstractCommand> commands;

    public Shell(Scanner scanner, ICommandParser parser, Map<CommandsEnum, AbstractCommand> commands) {
        this.scanner = scanner;
        this.parser = parser;
        this.commands = commands;
    }

    public int start() {
        var none = new NoneCommand();
        String version = "0.1.0";
        String header = "=====================================\n" +
                "        MemAllocShell v" + version + "\n" +
                "  A memory allocation simulator shell\n" +
                "=====================================\n";
        System.out.println(header);

        var running = true;
        while (running) {
            String commandStr = readCommand();

            var result = parser.parse(commandStr);
            if (!result.isSuccess()) {
                var error = result.getErrorOrElseThrow();
                System.out.println("Error on parse command: " + error.message());
                continue;
            }

            var parsedData = result.getResultOrElseThrow();

            if (parsedData.command().equals(CommandsEnum.EXIT)) {
                running = false;
                System.out.println("Exiting MemAllocShell. Goodbye!");
                continue;
            }

            var command = commands.getOrDefault(parsedData.command(), none);
            var commandResult = command.execute(parsedData.args());

            if (!commandResult.getMessage().isBlank()) {
                System.out.println(commandResult.getMessage());
            }
        }

        return 0;
    }

    private String readCommand() {
        System.out.print("> ");

        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return "exit";
        }
    }

    @Override
    public void close() {
        scanner.close();
    }
}
