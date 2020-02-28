package org.fangzz.alcumus.alcumusservice.dto;

import com.google.common.base.Strings;
import org.fangzz.alcumus.alcumusservice.model.Exercise;

public class ExerciseSummaryWithoutAnswer extends BaseDto {
    private String name;
    private String desc;
    private float difficulty = 0; //0到1之间
    private String from; //练习题摘录自哪里
    private String[] tags;

    public static ExerciseSummaryWithoutAnswer from(Exercise model) {
        if (null == model) {
            return null;
        }

        ExerciseSummaryWithoutAnswer dto = new ExerciseSummaryWithoutAnswer();
        BaseDto.convert(model, dto);
        if (!Strings.isNullOrEmpty(model.getTagNames())) {
            dto.setTags(model.getTagNames().split(","));
        } else {
            dto.setTags(new String[]{});
        }

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
