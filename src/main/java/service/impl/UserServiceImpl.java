package service.impl;

import entity.User;
import service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public void setChatId(User user, String chatId) {
        user.setChatId(chatId);
    }

    @Override
    public void setUserName(User user, String userName) {
        user.setUsername(userName);
    }

    @Override
    public void setRole(User user, String role) {
        user.setRole(role);
    }

    @Override
    public void setGroup(User user, String group) {
        user.setGroup(group);
    }
}
