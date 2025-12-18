package org.example.Parser.Validator;

import org.example.Enum.CommandsEnum;

public interface ICommandValidator {
    ValidationResult validate(CommandsEnum command, String[] args);
}