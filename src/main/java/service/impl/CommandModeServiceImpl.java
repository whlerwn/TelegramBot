package service.impl;

import entity.Command;
import service.CommandModeService;

import java.util.HashMap;
import java.util.Map;

public class CommandModeServiceImpl implements CommandModeService {
    private final Map<Long, Command> commands = new HashMap<>();

    public CommandModeServiceImpl() {
        System.out.println("CommandMode is created");
    }

    @Override
    public Command getCommand(long chatId) {
        return commands.getOrDefault(chatId, Command.START);
    }

    @Override
    public void setCommand(long chatId, Command command) {
        commands.put(chatId, command);
    }
}
