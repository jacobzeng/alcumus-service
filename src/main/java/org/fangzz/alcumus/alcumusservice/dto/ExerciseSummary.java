package org.fangzz.alcumus.alcumusservice.dto;

import com.google.common.base.Strings;
import org.fangzz.alcumus.alcumusservice.model.Exercise;

public class ExerciseSummary extends BaseDto {
    private String name;
    private String desc;
    private float difficulty = 0; //0到1之间
    private String answer; //答案
    private String answerDesc; //答案解析
    private String from; //练习题摘录自哪里
    private String[] tags;
    private boolean online = false;

    public static ExerciseSummary from(Exercise model) {
        if (null == model) {
            return null;
        }

        ExerciseSummary dto = new ExerciseSummary();
        BaseDto.convert(model, dto);
        if (!Strings.isNullOrEmpty(model.getTagNames())) {
            dto.setTags(model.getTagNames().split(","));
        } else {
            dto.setTags(new String[]{});
        }

        return dto;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
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
