package org.fangzz.alcumus.alcumusservice.web;

import org.fangzz.alcumus.alcumusservice.exception.UserRequiredException;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class UserAwareController {
    @Autowired
    private UserRepository userRepository;

    protected User currentUser() {
        return userRepository
                .findByUsernameAndDeletedIsFalse(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    protected User requireUser() {
        User result = currentUser();
        if (null == result) {
            throw new UserRequiredException();
        }
        return result;
    }
}
