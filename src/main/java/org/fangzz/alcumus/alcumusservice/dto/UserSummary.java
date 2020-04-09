package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.User;

public class UserSummary extends BaseDto {
    private String username;
    private String nickname;

    public final static UserSummary from(User model) {
        if (null == model) {
            return null;
        }
        UserSummary dto = new UserSummary();
        BaseDto.convert(model, dto);
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
}
