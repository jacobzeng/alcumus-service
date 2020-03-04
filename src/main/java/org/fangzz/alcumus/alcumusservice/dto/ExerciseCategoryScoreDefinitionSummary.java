package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategoryScoreDefinition;

public class ExerciseCategoryScoreDefinitionSummary extends BaseDto {
    private ExerciseCategorySummary category;
    private int status;
    private int score;

    public static ExerciseCategoryScoreDefinitionSummary from(ExerciseCategoryScoreDefinition model) {
        if (null == model) {
            return null;
        }

        ExerciseCategoryScoreDefinitionSummary dto = new ExerciseCategoryScoreDefinitionSummary();
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
