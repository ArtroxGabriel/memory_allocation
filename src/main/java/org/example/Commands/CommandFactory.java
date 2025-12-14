package org.example.Commands;

import org.example.Commands.Impl.*;
import org.example.Enum.CommandsEnum;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    public static Map<CommandsEnum, AbstractCommand> createCommands(org.example.Memory.AbstractMemoryManagement memory) {
        return new HashMap<>() {
            {
                put(CommandsEnum.INIT, new InitCommand(memory));
                put(CommandsEnum.ALLOC, new AllocCommand(memory));
                put(CommandsEnum.FREE_ID, new FreeIdCommand(memory));
                put(CommandsEnum.SHOW, new ShowCommand(memory));
                put(CommandsEnum.STATS, new StatsCommand(memory));
                put(CommandsEnum.RESET, new ResetCommand(memory));
                put(CommandsEnum.HELP, new HelpCommand());
            }
        };
    }


}

