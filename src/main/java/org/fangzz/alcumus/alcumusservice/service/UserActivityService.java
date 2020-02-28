package org.fangzz.alcumus.alcumusservice.service;

import org.fangzz.alcumus.alcumusservice.dto.param.UserActivityQueryParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface UserActivityService {
    UserActivity addActivity(@NotNull User student, @NotEmpty String content);

    Page<UserActivity> query(@NotNull @Validated UserActivityQueryParameter parameter, @NotNull User user);
}
