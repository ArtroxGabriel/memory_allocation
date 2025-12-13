package org.example.shell;

import lombok.extern.log4j.Log4j2;
import org.example.Parser.CommandParser;
import org.example.Parser.ICommandParser;

import java.util.Scanner;

@Log4j2
public class Shell implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);
    private final ICommandParser parser = new CommandParser();

    public int start() {
        String version = "0.1.0";
        String header = "=====================================\n" +
                "        MemAllocShell v" + version + "\n" +
                "  A memory allocation simulator shell\n" +
                "=====================================\n";
        System.out.println(header);

        var running = true;
        while (running) {
            String command = readCommand();

            var result = parser.parse(command);
            if (!result.isSuccess()) {
                var error = result.getErrorOrElseThrow();
                System.out.println("Erro ao analisar o comando: " + error.message());
                continue;
            }

            var parsedData = result.getResultOrElseThrow();
            System.out.println("Comando analisado: " + parsedData);
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
