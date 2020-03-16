package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.UserCategory;

public class UserCategorySummary extends BaseDto {
    private ExerciseCategorySummary category;
    private boolean current;
    private int counterOfFirstRight = 0;
    private int counterOfWrong = 0;
    private int counterOfSecondRight = 0;
    private int counterOfGiveup = 0;
    private int score = 0;
    private int difficultyLevel = 1;
    private int userLevel = 0;

    public final static UserCategorySummary from(UserCategory model) {
        if (null == model) {
            return null;
        }
        UserCategorySummary dto = new UserCategorySummary();
        BaseDto.convert(model, dto);
        dto.setCategory(ExerciseCategorySummary.from(model.getCategory()));
        return dto;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public ExerciseCategorySummary getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategorySummary category) {
        this.category = category;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getCounterOfFirstRight() {
        return counterOfFirstRight;
    }

    public void setCounterOfFirstRight(int counterOfFirstRight) {
        this.counterOfFirstRight = counterOfFirstRight;
    }

    public int getCounterOfWrong() {
        return counterOfWrong;
    }

    public void setCounterOfWrong(int counterOfWrong) {
        this.counterOfWrong = counterOfWrong;
    }

    public int getCounterOfSecondRight() {
        return counterOfSecondRight;
    }

    public void setCounterOfSecondRight(int counterOfSecondRight) {
        this.counterOfSecondRight = counterOfSecondRight;
    }

    public int getCounterOfGiveup() {
        return counterOfGiveup;
    }

    public void setCounterOfGiveup(int counterOfGiveup) {
        this.counterOfGiveup = counterOfGiveup;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
