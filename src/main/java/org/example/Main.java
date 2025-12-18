package org.example;

import org.example.Commands.CommandFactory;
import org.example.Enum.CommandsEnum;
import org.example.Memory.AbstractMemoryManagement;
import org.example.Memory.MemoryManagement;
import org.example.Parser.CommandParser;
import org.example.Parser.ICommandParser;
import org.example.Parser.Validator.CommandValidator;
import org.example.Parser.Validator.ICommandValidator;
import org.example.shell.Shell;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int exitCode;

        Scanner scanner = new Scanner(System.in);
        AbstractMemoryManagement memoryManagement = new MemoryManagement();
        ICommandValidator validator = new CommandValidator();
        ICommandParser parser = new CommandParser(validator);
        var commands = CommandFactory.createCommands(memoryManagement);

        try (var shell = new Shell(scanner, parser, commands)) {
            exitCode = shell.start();
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}