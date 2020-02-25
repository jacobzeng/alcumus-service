package org.fangzz.alcumus.alcumusservice.dto.param;

import org.fangzz.alcumus.alcumusservice.model.UserRole;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserCreateParameter {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "显示名不能为空")
    private String nickname;
    @NotEmpty(message = "密码不能为空")
    @Size(max = 20, min = 6, message = "密码长度在6到20位")
    private String password;

    private UserRole[] roles;

    public UserRole[] getRoles() {
        return roles;
    }

    public void setRoles(UserRole[] roles) {
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
