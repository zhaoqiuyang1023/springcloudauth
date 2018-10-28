package com.forezp.service;

import com.forezp.domain.User;
import com.forezp.repository.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImpl implements UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDao userDao;


    @Override
    public void create(User user) {

        User exist = userDao.findByUsername(user.getUsername());//默认没有此用户
        if (exist == null) {
            String hash = encoder.encode(user.getPassword());
            user.setPassword(hash);
            userDao.save(user);
        } else {
            log.error("此用户已经存在");
        }

    }
}
