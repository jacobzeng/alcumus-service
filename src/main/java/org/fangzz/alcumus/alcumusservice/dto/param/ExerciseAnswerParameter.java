package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ExerciseAnswerParameter {
    @NotNull(message = "您的习题不能为空")
    private Integer exerciseId;

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    @Size(min = 1, message = "您的答案不能为空")
    private String[] answers;


    private int counterOfRetry = 1;


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
