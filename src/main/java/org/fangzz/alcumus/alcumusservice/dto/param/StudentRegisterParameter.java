package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class StudentRegisterParameter {
    @NotEmpty(message = "您的登录名不能为空")
    @Size(min = 4, max = 30, message = "长度为4到30个字符")
    private String username;
    @NotEmpty(message = "您的昵称不能为空")
    @Size(min = 4, max = 30, message = "长度为4到30个字符")
    private String nickname;
    @NotEmpty(message = "您的密码不能为空")
    @Size(min = 6, max = 20, message = "长度为6到20个字符")
    private String password;
    private String passwordAgain;

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

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}
