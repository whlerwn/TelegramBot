package service;

import entity.Command;
import service.impl.CommandModeServiceImpl;

public interface CommandModeService {

    static CommandModeService getInstance() {
        return new CommandModeServiceImpl();
    }

    Command getCommand(long chatId);

    void setCommand(long chatId, Command command);
}
