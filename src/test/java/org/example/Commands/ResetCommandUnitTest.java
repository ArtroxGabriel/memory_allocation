package org.example.Commands;

import org.example.Commands.Impl.ResetCommand;
import org.example.Memory.AbstractMemoryManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResetCommandUnitTest {
    private ResetCommand resetCommand;

    @Mock
    private AbstractMemoryManagement mockMemory;

    @BeforeEach
    public void setUp() {
        resetCommand = new ResetCommand(mockMemory);
    }

    @Test
    @DisplayName("Should successfully reset memory when initialized")
    void execute_InitializedMemory_Success() {
        // ARRANGE
        String[] args = {};

        when(mockMemory.isInitialized()).thenReturn(true);

        // ACT
        CommandsResult result = resetCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("Memory has been reset.");

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, times(1)).resetMemory();
    }

    @Test
    @DisplayName("Should fail if memory is not initialized")
    void execute_MemoryNotInitialized_Failure() {
        // ARRANGE
        String[] args = {};

        when(mockMemory.isInitialized()).thenReturn(false);

        // ACT
        CommandsResult result = resetCommand.execute(args);

        // ASSERT
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("Memory is not initialized.");

        verify(mockMemory, times(1)).isInitialized();
        verify(mockMemory, never()).resetMemory();
    }
}
