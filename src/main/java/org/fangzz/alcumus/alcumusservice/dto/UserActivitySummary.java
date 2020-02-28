package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.UserActivity;

public class UserActivitySummary extends BaseDto {
    private String log;

    public static UserActivitySummary from(UserActivity model) {
        if (null == model) {
            return null;
        }

        UserActivitySummary dto = new UserActivitySummary();
        BaseDto.convert(model, dto);
        return dto;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
