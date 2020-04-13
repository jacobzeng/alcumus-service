package org.fangzz.alcumus.alcumusservice.service;


import org.fangzz.alcumus.alcumusservice.dto.param.StudentRegisterParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserCreateParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserQueryParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserUpdateParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface UserService {
    User findByUsername(@NotEmpty String username);

    User createUser(@NotNull @Valid UserCreateParameter parameter);

    User createStudentAccount(@NotNull @Valid StudentRegisterParameter parameter);

    User findById(@NotNull Integer id);

    Page<User> query(@NotNull UserQueryParameter parameter);

    User updateUser(@NotNull Integer id, @NotNull @Valid UserUpdateParameter parameter, @NotNull User requireUser);

    User createUser(@NotNull @Valid UserCreateParameter parameter, @NotNull User requireUser);

    void deleteUser(@NotNull Integer id, @NotNull User requireUser);
}
