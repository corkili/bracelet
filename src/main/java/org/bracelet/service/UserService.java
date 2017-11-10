package org.bracelet.service;

import org.bracelet.common.model.Result;
import org.bracelet.entity.Message;
import org.bracelet.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    Result login(String phone, String password, HttpSession session);

    void logout(long userId, HttpSession session);

    Result register(User user);

    User getUser(long userId);

    User getUser(String phone);

    Result modifyUserInformation(User user);

    Result modifyPassword(String phone, String password);

    Result sendMessage(String content, long fromUserId, long toUserId, long time);

    Result addFriend(long fromUserId, String phone);
}
