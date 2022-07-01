package service;

import service.impl.NameServiceImpl;

public interface NameService {

    static NameService getInstance() {
        return new NameServiceImpl();
    }

    String getNameValue(String name);
}
