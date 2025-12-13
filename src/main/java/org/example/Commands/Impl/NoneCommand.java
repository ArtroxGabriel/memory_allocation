package org.example.Commands.Impl;

import org.example.Commands.AbstractCommand;
import org.example.Commands.CommandsResult;

public class NoneCommand extends AbstractCommand {
    public NoneCommand() {
        super(null);
    }

    @Override
    public CommandsResult execute(String[] args) {
        return CommandsResult.Success();
    }

    @Override
    protected CommandsResult validateArgs(String[] args) {
        return null;
    }
}
