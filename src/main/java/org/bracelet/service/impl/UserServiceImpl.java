package org.bracelet.service.impl;

import org.bracelet.common.model.Result;
import org.bracelet.common.session.SessionContext;
import org.bracelet.common.utils.AgeUtil;
import org.bracelet.common.utils.HashUtil;
import org.bracelet.dao.UserDao;
import org.bracelet.entity.User;
import org.bracelet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private SessionContext sessionContext;

    private static final String CHECK_PHONE = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$";

    private UserContext userContext;

    public UserServiceImpl() {
        userContext = new UserContext();
        sessionContext = SessionContext.getInstance();
    }

    @Override
    public Result login(String phone, String password, HttpSession session) {
        boolean successful = false;
        String message;
        User user = null;
        if (StringUtils.isEmpty(phone)) {
            message = "手机号不能为空";
        } else if (StringUtils.isEmpty(password)) {
            message = "密码不能为空";
        } else {
            user = userDao.findUserByPhone(phone);
            if (user == null || !HashUtil.verify(password, user.getPassword())) {
                message = "用户或密码错误";
            } else {
                userContext.login(user);
                session.setAttribute(SessionContext.ATTR_USER_ID, String.valueOf(user.getId()));
                session.setAttribute(SessionContext.ATTR_USER_NAME, user.getUsername());
                sessionContext.sessionHandlerByCacheMap(session);
                successful = true;
                message = "登录成功";
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return null;
    }

    @Override
    public void logout(long userId, HttpSession session) {
        userContext.logout(userId);
        session.invalidate();
    }

    @Override
    public Result register(User user) {
        boolean successful = false;
        String message;
        if (StringUtils.isEmpty(user.getPhone())
                || !Pattern.compile(CHECK_PHONE).matcher(user.getPhone()).matches()) {
            message = "手机号无效";
        } else if (userDao.existUser(user.getPhone())) {
            message = "该手机号已被注册";
        } else if (StringUtils.isEmpty(user.getPassword())) {
            message = "密码不能为空";
        } else {
            if (user.getBirthday() == null) {
                user.setBirthday(new Date(System.currentTimeMillis()));
            }
            user.setAge(AgeUtil.getAgeByBirth(user.getBirthday()));
            if (user.getUsername() == null) {
                user.setUsername("m" + user.getPhone());
            }
            if (user.getName() == null) {
                user.setName("匿名用户");
            }
            user.setRegisterTime(new java.util.Date());
            user.setLastLoginTime(new java.util.Date());
            if (userDao.save(user) == null) {
                message = "数据库忙，请重试";
            } else {
                successful = true;
                message = "注册成功";
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public User getUser(long userId) {
        User user = userContext.get(userId);
        if (user == null) {
            user = userDao.get(userId);
        }
        return user;
    }

    @Override
    public User getUser(String phone) {
        return userDao.findUserByPhone(phone);
    }

    @Override
    public void modifyUserInformation(User user) {
        userDao.saveOrUpdate(user);
        userContext.update(user);
    }

    @Override
    public Result modifyPassword(String phone, String password) {
        boolean successful = false;
        String message;
        User user = userDao.findUserByPhone(phone);
        if (user == null) {
            message = "该手机号未注册";
        } else if (StringUtils.isEmpty(password)){
            message = "新密码不能玩为空";
        } else {
            user.setPassword(HashUtil.generate(password));
            userDao.saveOrUpdate(user);
            successful = true;
            message = "修改密码成功";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private class UserContext {

        private Map<Long, User> loginUsers;

        private UserContext() {
            loginUsers = new HashMap<>();
        }

        public User get(long userId) {
            return loginUsers.get(userId);
        }

        public void login(User user) {
            loginUsers.put(user.getId(), user);
        }

        public void logout(long userId) {
            loginUsers.remove(userId);
        }

        public void update(User... users) {
            for (User user : users) {
                if (user != null && loginUsers.containsKey(user.getId())) {
                    loginUsers.put(user.getId(), user);
                }
            }
        }

        public void update(List<User> users) {
            for (User user : users) {
                if (user != null && loginUsers.containsKey(user.getId())) {
                    loginUsers.put(user.getId(), user);
                }
            }
        }
    }
}
