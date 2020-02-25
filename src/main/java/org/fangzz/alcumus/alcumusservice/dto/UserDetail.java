package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserRole;

public class UserDetail extends BaseDto {
    private String username;
    private String nickname;
    private UserRole[] roles;

    public static UserDetail from(User model) {
        if (null == model) {
            return null;
        }

        UserDetail dto = new UserDetail();
        BaseDto.convert(model, dto);
        dto.setRoles(model.getRoles().toArray(new UserRole[]{}));
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserRole[] getRoles() {
        return roles;
    }

    public void setRoles(UserRole[] roles) {
        this.roles = roles;
    }
}
