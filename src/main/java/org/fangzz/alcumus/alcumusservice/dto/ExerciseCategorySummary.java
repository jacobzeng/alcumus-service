package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;

public class ExerciseCategorySummary extends BaseDto {
    private String name;
    private String code;

    public static ExerciseCategorySummary from(ExerciseCategory model) {
        if (null == model) {
            return null;
        }
        ExerciseCategorySummary dto = new ExerciseCategorySummary();
        BaseDto.convert(model, dto);
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
