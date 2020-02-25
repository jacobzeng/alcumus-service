package org.fangzz.alcumus.alcumusservice.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fangzz.alcumus.alcumusservice.dto.param.UserCreateParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserRole;
import org.fangzz.alcumus.alcumusservice.repository.UserRepository;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Validated
@Service
public class UserServiceImpl implements UserService {
    private final static Log log = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(@NotEmpty String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createUser(@NotNull @Valid UserCreateParameter parameter) {
        log.info(String.format("create user %s", parameter.getUsername()));
        User user = new User();
        user.setUsername(parameter.getUsername());
        user.setNickname(parameter.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(parameter.getPassword()));
        if (parameter.getRoles().length == 0) {
            user.setRoles(Arrays.asList(UserRole.ROLE_USER));
        } else {
            user.setRoles(Arrays.asList(parameter.getRoles()));
        }
        return userRepository.save(user);
    }
}
