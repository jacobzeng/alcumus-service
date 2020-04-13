package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotEmpty;

public class UserUpdateParameter {
    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    private String password;

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
