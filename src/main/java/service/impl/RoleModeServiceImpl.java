package service.impl;

import entity.Role;
import service.RoleModeService;

import java.util.HashMap;
import java.util.Map;

public class RoleModeServiceImpl implements RoleModeService {
    private final Map<Long, Role> roles = new HashMap<>();

    public RoleModeServiceImpl() {
        System.out.println("RoleMode is created");
    }

    @Override
    public Role getRole(long chatId) {
        return roles.getOrDefault(chatId, Role.CLIENT);
    }

    @Override
    public void setRole(long chatId, Role role) {
        roles.put(chatId, role);
    }
}
