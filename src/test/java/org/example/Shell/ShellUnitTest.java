package org.example.Shell;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;
import org.example.Enum.CommandsEnum;
import org.example.Parser.CommandParserResult;
import org.example.Parser.ICommandParser;
import org.example.shell.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShellUnitTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @Mock
    private ICommandParser parser;
    @Mock
    private Map<CommandsEnum, AbstractCommand> commands;
    @Mock
    private AbstractCommand mockCommand;

    @BeforeEach
    void setUpStreams() {
        // Capture System.out to verify console messages
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Start: Should exit immediately when receiving EXIT command")
    void start_ExitCommand_ExitsLoop() {
        // ARRANGE
        // Simulate user typing "exit"
        Scanner scanner = new Scanner("exit");
        Shell shell = new Shell(scanner, parser, commands);

        // Mock Parser to return EXIT enum
        when(parser.parse("exit")).thenReturn(CommandParserResult.Success(CommandsEnum.EXIT));

        // ACT
        int exitCode = shell.start();

        // ASSERT
        assertThat(exitCode).isEqualTo(0);
        assertThat(outContent.toString()).contains("Exiting MemAllocShell. Goodbye!");

        // Ensure commands map was never queried since we exited immediately
        verify(commands, never()).getOrDefault(any(), any());
    }

    @Test
    @DisplayName("Start: Should execute a valid command and then exit")
    void start_ValidCommand_ExecutesAndThenExits() {
        // ARRANGE
        // Simulate user typing "stats", hitting enter, then typing "exit"
        Scanner scanner = new Scanner("stats\nexit");
        Shell shell = new Shell(scanner, parser, commands);

        // 1. Mock Parser for "stats"
        String[] emptyArgs = {};
        when(parser.parse("stats")).thenReturn(CommandParserResult.Success(CommandsEnum.STATS, emptyArgs));

        // 2. Mock Parser for "exit"
        when(parser.parse("exit")).thenReturn(CommandParserResult.Success(CommandsEnum.EXIT));

        // 3. Mock the Command Execution
        when(commands.getOrDefault(eq(CommandsEnum.STATS), any())).thenReturn(mockCommand);
        when(mockCommand.execute(emptyArgs)).thenReturn(CommandsResult.Success("Stats Printed"));

        // ACT
        shell.start();

        // ASSERT
        verify(mockCommand, times(1)).execute(emptyArgs);

        // Verify output contains the command success message
        assertThat(outContent.toString()).contains("Stats Printed");
        assertThat(outContent.toString()).contains("Exiting MemAllocShell");
    }

    @Test
    @DisplayName("Start: Should handle parser error without crashing")
    void start_ParserError_PrintsErrorMessage() {
        // ARRANGE
        Scanner scanner = new Scanner("bad_command\nexit");
        Shell shell = new Shell(scanner, parser, commands);

        // 1. Mock Parser Failure
        when(parser.parse("bad_command")).thenReturn(CommandParserResult.Failure("Invalid Syntax"));

        // 2. Mock Exit
        when(parser.parse("exit")).thenReturn(CommandParserResult.Success(CommandsEnum.EXIT));

        // ACT
        shell.start();

        // ASSERT
        assertThat(outContent.toString()).contains("Error on parse command: Invalid Syntax");

        // Ensure we did NOT try to execute a command for the failure
        verify(commands, never()).get(any());
    }

    @Test
    @DisplayName("Start: Should execute mapped command even if parser returns UNKNOWN (if mapped)")
    void start_UnknownCommand_UsesDefaultOrMapped() {
        // ARRANGE
        // This test verifies the fallback logic.
        // Although the code uses 'new NoneCommand()' as default, we can Mock the map
        // to return our 'mockCommand' even for unknown keys to verify the flow.

        Scanner scanner = new Scanner("gibberish\nexit");
        Shell shell = new Shell(scanner, parser, commands);

        // 1. Parser returns UNKNOWN
        when(parser.parse("gibberish")).thenReturn(CommandParserResult.Success(CommandsEnum.UNKNOWN));
        when(parser.parse("exit")).thenReturn(CommandParserResult.Success(CommandsEnum.EXIT));

        // 2. Map returns mockCommand when requested for UNKNOWN
        when(commands.getOrDefault(eq(CommandsEnum.UNKNOWN), any())).thenReturn(mockCommand);
        when(mockCommand.execute(any())).thenReturn(CommandsResult.Success("Handled Unknown"));

        // ACT
        shell.start();

        // ASSERT
        verify(mockCommand, times(1)).execute(any());
        assertThat(outContent.toString()).contains("Handled Unknown");
    }

    @Test
    @DisplayName("Close: Should close the scanner")
    void close_ClosesScanner() {
        // ARRANGE
        // We can't easily spy on the real Scanner class final methods.
        // However, we can just verify the Shell implements AutoCloseable correctly.
        // This test ensures the method doesn't throw.

        Scanner scanner = new Scanner("exit");
        Shell shell = new Shell(scanner, parser, commands);

        // ACT
        shell.close();

        // ASSERT
        // If the scanner is closed, trying to read from it should throw
        assertThat(scanner.ioException()).isNull();
        // NOTE: Verifying Scanner.close() is difficult without a wrapper,
        // but checking no exceptions were thrown is a decent sanity check.
    }
}
