package org.example.Commands;


import lombok.AllArgsConstructor;
import org.example.Memory.AbstractMemoryManagement;

@AllArgsConstructor
public abstract class AbstractCommand {

    protected final AbstractMemoryManagement memory;

    /**
     * Executes the command with the given arguments
     *
     * @param args the arguments for the command
     * @return result {@link CommandsResult} of the execution
     */
    abstract public CommandsResult execute(String[] args);

    /**
     * Validates the command arguments
     *
     * @param args the arguments to validate
     * @return result {@link CommandsResult} of the validation
     */
    abstract protected CommandsResult validateArgs(String[] args);
}
