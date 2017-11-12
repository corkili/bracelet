package org.bracelet.service.impl;

import org.apache.log4j.Logger;
import org.bracelet.common.model.Result;
import org.bracelet.common.session.SessionContext;
import org.bracelet.common.utils.AgeUtil;
import org.bracelet.common.utils.FoodHelper;
import org.bracelet.common.utils.HashUtil;
import org.bracelet.dao.UserDao;
import org.bracelet.entity.Message;
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

    private Logger logger = Logger.getLogger(UserServiceImpl.class);

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
                int age = AgeUtil.getAgeByBirth(user.getBirthday());
                if (age != user.getAge()) {
                    user.setAge(age);
                }
                user.setLastLoginTime(new java.util.Date());
                userDao.saveOrUpdate(user);
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
        return result;
    }

    @Override
    public void logout(long userId, HttpSession session) {
        userContext.logout(userId);
        session.invalidate();
    }

    @Override
    public Result register(User user) {
        logger.info(user.toString());
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
            user.setPassword(HashUtil.generate(user.getPassword()));
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
    public Result modifyUserInformation(User user) {
        String message = "修改成功";
        userDao.saveOrUpdate(user);
        userContext.update(user);
        Result result = new Result(true);
        result.setMessage(message);
        result.add("user", user);
        return result;
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
            userContext.update(user);
            successful = true;
            message = "修改密码成功";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public Result sendMessage(String content, long fromUserId, long toUserId, long time) {
        boolean successful = false;
        String message;
        User fromUser = getUser(fromUserId);
        User toUser = getUser(toUserId);
        if (fromUser == null || toUser == null) {
            message = "发送方或接收方有误";
        } else {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromUserId(fromUser.getId());
            msg.setFromUserName(fromUser.getName());
            msg.setFromUserPhone(fromUser.getPhone());
            msg.setTime(new java.util.Date(time));
            toUser.getMessages().add(msg);

            Message res = new Message();
            res.setContent("已收到您的消息");
            res.setFromUserId(toUser.getId());
            res.setFromUserName(toUser.getName());
            res.setFromUserPhone(toUser.getPhone());
            res.setTime(new java.util.Date(time));
            fromUser.getMessages().add(msg);

            userDao.saveOrUpdate(toUser);
            userDao.saveOrUpdate(fromUser);
            userContext.update(toUser, fromUser);
            successful = true;
            message = "消息发送成功";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        return result;
    }

    @Override
    public Result addFriend(long fromUserId, String phone) {
        boolean successful = false;
        String message;
        User fromUser = getUser(fromUserId);
        User toUser = getUser(phone);
        if (fromUser == null || toUser == null) {
            message = "不存在手机号为" + phone + "的用户";
        } else {
            fromUser.getFriends().add(toUser);
            Message msg = new Message();
            msg.setContent("用户" + fromUser.getName() + "(" + fromUser.getPhone() + ")已将您添加为好友！");
            msg.setFromUserId(fromUser.getId());
            msg.setFromUserName(fromUser.getName());
            msg.setFromUserPhone(fromUser.getPhone());
            msg.setTime(new java.util.Date());
            toUser.getMessages().add(msg);
            userDao.saveOrUpdate(fromUser);
            userDao.saveOrUpdate(toUser);
            userContext.update(fromUser, toUser);
            successful = true;
            message = "添加好友成功";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", fromUser);
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
