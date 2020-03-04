package org.fangzz.alcumus.alcumusservice.dto.param;

import org.fangzz.alcumus.alcumusservice.model.UserExerciseLog;

import javax.validation.constraints.NotNull;

public class ExerciseCategoryScoreDefinitionCreateParameter {
    @NotNull(message = "请提供习题分类")
    private Integer categoryId;
    private int status = UserExerciseLog.STATUS_RIGHT_FIRST_TIME;
    private int score;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
