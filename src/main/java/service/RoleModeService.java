package service;

import entity.Role;
import service.impl.RoleModeServiceImpl;

public interface RoleModeService {

    static RoleModeService getInstance() {
        return new RoleModeServiceImpl();
    }

    Role getRole(long chatId);

    void setRole(long chatId, Role role);
}
