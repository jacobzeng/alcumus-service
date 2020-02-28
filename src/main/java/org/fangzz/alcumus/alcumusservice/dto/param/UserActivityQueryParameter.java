package org.fangzz.alcumus.alcumusservice.dto.param;

public class UserActivityQueryParameter extends PageQueryParameter{
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
