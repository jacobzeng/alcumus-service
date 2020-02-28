package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ExerciseAnswerParameter {
    @NotNull(message = "您的习题不能为空")
    private Integer exerciseId;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @NotEmpty(message = "您的答案不能为空")
    private String answer;

    private int counterOfRetry =0;

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getCounterOfRetry() {
        return counterOfRetry;
    }

    public void setCounterOfRetry(int counterOfRetry) {
        this.counterOfRetry = counterOfRetry;
    }
}
