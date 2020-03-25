package org.fangzz.alcumus.alcumusservice.dto;

import java.util.List;

public class StudentReport2 {
    private UserCategorySummary secondUserCategory;
    private List<UserCategorySummary> thirdUserCategories;
    private List<UserExerciseLogSummary> exerciseLogs;

    public UserCategorySummary getSecondUserCategory() {
        return secondUserCategory;
    }

    public void setSecondUserCategory(UserCategorySummary secondUserCategory) {
        this.secondUserCategory = secondUserCategory;
    }

    public List<UserCategorySummary> getThirdUserCategories() {
        return thirdUserCategories;
    }

    public void setThirdUserCategories(
            List<UserCategorySummary> thirdUserCategories) {
        this.thirdUserCategories = thirdUserCategories;
    }

    public List<UserExerciseLogSummary> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<UserExerciseLogSummary> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }
}
