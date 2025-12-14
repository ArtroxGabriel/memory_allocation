package org.example.Commands;

import org.example.Commands.Impl.InitCommand;
import org.example.Memory.AbstractMemoryManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InitCommandUnitTest {
    private InitCommand initCommand;

    @Mock
    private AbstractMemoryManagement mockMemory;

    @BeforeEach
    public void setUp() {
        initCommand = new InitCommand(mockMemory);
    }

    @Test
    @DisplayName("Should successfully initialize memory when uninitialized")
    void execute_UninitializedMemory_Success() {
        // ARRANGE
        String[] args = {"1024"};

        when(mockMemory.isInitialized()).thenReturn(false);

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEmpty();

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, times(1)).initMemory(1024);
    }

    @Test
    @DisplayName("Should fail if memory is already initialized")
    void execute_AlreadyInitializedMemory_Failure() {
        // ARRANGE
        String[] args = {"512"};

        when(mockMemory.isInitialized()).thenReturn(true);

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Memory has already been initialized.");

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, never()).initMemory(anyInt());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "10.5", "0xG"})
    @DisplayName("Should fail for non-integer size argument")
    void validateArgs_InvalidNumberFormat_Failure(String invalidArg) {
        // ARRANGE
        String[] args = {invalidArg};

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("not a valid integer");

        verify(mockMemory, never()).isInitialized();
        verify(mockMemory, never()).initMemory(anyInt());
    }

    @Test
    @DisplayName("Validation: Should fail if argument count is incorrect (too many)")
    void validateArgs_TooManyArguments_Failure() {
        // ARRANGE
        String[] args = {"100", "extra"};

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("requires exactly one argument");

        verify(mockMemory, never()).isInitialized();
    }

    @Test
    @DisplayName("Validation: Should fail if argument count is incorrect (too few)")
    void validateArgs_TooFewArguments_Failure() {
        // ARRANGE
        String[] args = {};

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("requires exactly one argument");

        verify(mockMemory, never()).isInitialized();
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -5L, -1024L})
    @DisplayName("Validation: Should fail for zero or negative memory size")
    void validateArgs_ZeroOrNegativeSize_Failure(long size) {
        // ARRANGE
        String[] args = {String.valueOf(size)};

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("must be greater than zero");

        verify(mockMemory, never()).isInitialized();
    }

    @Test
    @DisplayName("Validation: Should fail for size argument exceeding Integer.MAX_VALUE")
    void validateArgs_SizeTooLarge_Failure() {
        // ARRANGE
        String largeValue = String.valueOf((long) Integer.MAX_VALUE + 10L);
        String[] args = {largeValue};

        // ACT
        CommandsResult result = initCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("First argument <size> is too large. Maximum allowed is " + Integer.MAX_VALUE);

        verify(mockMemory, never()).isInitialized();
    }
}
