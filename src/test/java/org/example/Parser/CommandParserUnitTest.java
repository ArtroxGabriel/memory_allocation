package org.example.Parser;

import org.example.Enum.CommandsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandParserUnitTest {
    private ICommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new CommandParser();
    }

    @Test
    public void parse_validCommandWithArgs_returnsAllocAndArgs() {
        // ARRANGE
        String input = "alloc 10 20 30";

        // ACT
        var result = parser.parse(input);

        // ASSERT
        assertThat(result.isSuccess()).isTrue();

        var data = result.getResultOrElseThrow();
        assertThat(data.command()).isEqualTo(CommandsEnum.ALLOC);
        assertThat(data.args()).containsExactly("10");
    }

    @Test
    public void parse_validCommandNoArgs_returnsStatsWithEmptyArgs() {
        // ARRANGE
        String input = "stats";

        // ACT
        var result = parser.parse(input);


        // ASSERT
        assertThat(result.isSuccess()).isTrue();

        var data = result.getResultOrElseThrow();
        assertThat(data.command()).isEqualTo(CommandsEnum.STATS);
        assertThat(data.args()).isEmpty();
    }

    @Test
    public void parse_mixedCaseAndExtraSpaces_parsesCorrectly() {
        // ARRANGE
        String input = "  AlLoC   1 2  ";

        // ACT
        var result = parser.parse(input);

        // ASSERT
        assertThat(result.isSuccess()).isTrue();

        var data = result.getResultOrElseThrow();
        assertThat(data.command()).isEqualTo(CommandsEnum.ALLOC);
        assertThat(data.args()).containsExactly("1", "2");
    }

    @Test
    public void parse_emptyOrWhitespace_returnsDoNothing() {
        // ARRANGE
        String input1 = "";
        String input2 = "   ";

        // ACT
        var result1 = parser.parse(input1);
        var result2 = parser.parse(input2);

        // ASSERT
        assertThat(result1.isSuccess()).isTrue();

        var data1 = result1.getResultOrElseThrow();
        assertThat(data1.command()).isEqualTo(CommandsEnum.DO_NOTHING);


        assertThat(result2.isSuccess()).isTrue();

        var data2 = result2.getResultOrElseThrow();
        assertThat(data2.command()).isEqualTo(CommandsEnum.DO_NOTHING);
    }

    @Test
    public void parse_unknownCommand_returnsFailureWithMessage() {
        // ARRANGE
        String input = "foo";

        // ACT
        var result = parser.parse(input);

        // ARRANGE
        assertThat(result.isSuccess()).isFalse();

        var error = result.getErrorOrElseThrow();
        assertThat(error.message()).startsWith("Unknown command:");
        assertThat(error.message()).contains("foo");
    }
}

