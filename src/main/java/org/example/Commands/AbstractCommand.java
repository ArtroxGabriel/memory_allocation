package org.example.Commands;


import org.example.Memory.AbstractMemoryManagement;

public abstract class AbstractCommand {

    protected final AbstractMemoryManagement memory;

    public AbstractCommand(AbstractMemoryManagement memory) {
        this.memory = memory;
    }

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
    abstract public CommandsResult validate(String[] args);
}
