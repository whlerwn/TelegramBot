package service;

import entity.User;
import service.impl.UserServiceImpl;

public interface UserService {

    static UserService getInstance() {
        return new UserServiceImpl();
    }

    void setChatId(User user, String chatId);
    void setUserName(User user, String userName);
    void setRole(User user, String role);
    void setGroup(User user, String group);
}
