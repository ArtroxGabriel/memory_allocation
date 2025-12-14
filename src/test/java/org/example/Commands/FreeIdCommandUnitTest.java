package org.example.Commands;

import org.example.Commands.Impl.FreeIdCommand;
import org.example.Memory.AbstractMemoryManagement;
import org.example.Memory.MemoryManagementResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FreeIdCommandUnitTest {

    private FreeIdCommand freeIdCommand;

    @Mock
    private AbstractMemoryManagement mockMemory;

    @BeforeEach
    void setUp() {
        freeIdCommand = new FreeIdCommand(mockMemory);
    }


    @Test
    @DisplayName("Should successfully free memory for a valid ID")
    void execute_ValidId_Success() {
        // ARRANGE
        int targetId = 42;
        String[] args = {String.valueOf(targetId)};

        when(mockMemory.isInitialized()).thenReturn(true);
        when(mockMemory.freeMemory(targetId)).thenReturn(MemoryManagementResult.Success());

        // ACT
        CommandsResult result = freeIdCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEmpty();

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, times(1)).freeMemory(targetId);
    }


    @Test
    @DisplayName("Should fail if memory is not initialized")
    void execute_MemoryNotInitialized_Failure() {
        // ARRANGE
        String[] args = {"1"};
        when(mockMemory.isInitialized()).thenReturn(false);

        // ACT
        CommandsResult result = freeIdCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Memory is not initialized.");

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, never()).freeMemory(anyInt());
    }

    @Test
    @DisplayName("Should fail if memory.freeMemory returns a failure")
    void execute_MemoryFreeingFails_Failure() {
        // ARRANGE
        int targetId = 99;
        String[] args = {String.valueOf(targetId)};
        String mockErrorMessage = "Block ID 99 not found.";

        when(mockMemory.isInitialized()).thenReturn(true);
        when(mockMemory.freeMemory(targetId)).thenReturn(MemoryManagementResult.Failure(mockErrorMessage));

        // ACT
        CommandsResult result = freeIdCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Freeing memory failed: " + mockErrorMessage);

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, times(1)).freeMemory(targetId);
    }

    @Test
    @DisplayName("Should fail execution if argument count is incorrect")
    void execute_IncorrectArgsCount_Failure() {
        // ARRANGE
        String[] args = {};

        // ACT
        CommandsResult result = freeIdCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("requires exactly one argument");

        verify(mockMemory, never()).isInitialized();
    }

    @Test
    @DisplayName("Should fail execution for ID argument exceeding Integer.MAX_VALUE")
    void execute_IdTooLarge_Failure() {
        // ARRANGE
        String largeValue = String.valueOf((long) Integer.MAX_VALUE + 1L);
        String[] args = {largeValue};

        // ACT
        CommandsResult result = freeIdCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("is too large");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "10.5", "nan"})
    @DisplayName("Validation: Should fail execution for non-integer ID")
    void execute_InvalidNumberFormat_Failure(String invalidArg) {
        // ARRANGE
        String[] args = {invalidArg};

        // ACT
        CommandsResult result = freeIdCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("not a valid integer");
    }
}
