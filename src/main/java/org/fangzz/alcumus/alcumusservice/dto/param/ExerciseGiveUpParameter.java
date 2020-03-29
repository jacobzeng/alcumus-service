package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotNull;

public class ExerciseGiveUpParameter {
    @NotNull(message = "请提供习题的数据id")
    private Integer exerciseId;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }
}
