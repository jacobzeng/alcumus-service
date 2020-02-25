package org.fangzz.alcumus.alcumusservice.service;


import org.fangzz.alcumus.alcumusservice.dto.param.UserCreateParameter;
import org.fangzz.alcumus.alcumusservice.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface UserService {
    User findByUsername(@NotEmpty String username);

    User createUser(@NotNull @Valid UserCreateParameter parameter);
}
