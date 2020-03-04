package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.UserScore;

public class UserScoreSummary extends BaseDto {
    private ExerciseCategorySummary category;
    private int score;

    public static UserScoreSummary from(UserScore model) {
        if (null == model) {
            return null;
        }

        UserScoreSummary dto = new UserScoreSummary();
        BaseDto.convert(model, dto);
        dto.setCategory(ExerciseCategorySummary.from(model.getCategory()));
        return dto;
    }

    public ExerciseCategorySummary getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategorySummary category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
