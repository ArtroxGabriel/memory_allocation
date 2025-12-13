package org.example.shell;

import lombok.extern.log4j.Log4j2;
import org.example.Commands.AbstractCommand;
import org.example.Commands.InitCommand;
import org.example.Enum.CommandsEnum;
import org.example.Memory.AbstractMemoryManagement;
import org.example.Memory.MemoryManagement;
import org.example.Parser.CommandParser;
import org.example.Parser.ICommandParser;

import java.util.HashMap;
import java.util.Scanner;

@Log4j2
public class Shell implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);
    private final ICommandParser parser = new CommandParser();
    private final HashMap<CommandsEnum, AbstractCommand> commands = new HashMap<>();

    public Shell() {
        initialize();
    }

    public void initialize() {
        AbstractMemoryManagement memory = new MemoryManagement();

        commands.put(CommandsEnum.INIT, new InitCommand(memory));
    }

    public int start() {
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
                System.out.println("Erro ao analisar o comando: " + error.message());
                continue;
            }

            var parsedData = result.getResultOrElseThrow();

            if (parsedData.command().equals(CommandsEnum.EXIT)){
                running = false;
                System.out.println("Exiting MemAllocShell. Goodbye!");
                continue;
            }

            var command = commands.get(parsedData.command());
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
