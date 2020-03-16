package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotNull;

public class ExerciseGiveUpParameter {
    @NotNull(message = "请提供习题的数据id")
    private Integer exerciseId;

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }
}
