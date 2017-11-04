package org.bracelet.service;

import org.bracelet.common.model.Result;
import org.bracelet.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    Result login(String phone, String password, HttpSession session);

    void logout(long userId, HttpSession session);

    Result register(User user);
}
