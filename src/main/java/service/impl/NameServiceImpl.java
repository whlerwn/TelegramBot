package service.impl;

import service.NameService;

public class NameServiceImpl implements NameService {
    @Override
    public String getNameValue(String name) {
        return name;
    }
}
