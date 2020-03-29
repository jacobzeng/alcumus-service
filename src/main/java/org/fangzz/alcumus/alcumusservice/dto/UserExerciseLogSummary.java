package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.UserExerciseLog;

public class UserExerciseLogSummary extends BaseDto {
    private ExerciseSummary exercise;
    private int status;
    private String statusLabel;
    private String answer;
    private String answer2;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public static final UserExerciseLogSummary from(UserExerciseLog model) {
        if (null == model) {
            return null;
        }
        UserExerciseLogSummary dto = new UserExerciseLogSummary();
        BaseDto.convert(model, dto);
        dto.setExercise(ExerciseSummary.from(model.getExercise()));
        switch (dto.getStatus()) {
            case UserExerciseLog.STATUS_RIGHT_FIRST_TIME:
            case UserExerciseLog.STATUS_RIGHT_SECOND_TIME:
                dto.setStatusLabel("正确");
                break;
            case UserExerciseLog.STATUS_GIVE_UP:
                dto.setStatusLabel("放弃");
                break;
            case UserExerciseLog.STATUS_WRONG:
                dto.setStatusLabel("错误");
                break;
        }
        return dto;
    }

    public ExerciseSummary getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseSummary exercise) {
        this.exercise = exercise;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
