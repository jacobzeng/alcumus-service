package org.fangzz.alcumus.alcumusservice.dto;

import java.util.List;

public class StudentReport1 {
    private UserCategorySummary thirdUserCategory;
    private UserCategorySummary secondUserCategory;
    private List<UserCategorySummary> thirdUserCategories;
    private List<UserExerciseLogSummary> exerciseLogs;

    public List<UserExerciseLogSummary> getExerciseLogs() {
        return exerciseLogs;
    }

    public void setExerciseLogs(List<UserExerciseLogSummary> exerciseLogs) {
        this.exerciseLogs = exerciseLogs;
    }

    public List<UserCategorySummary> getThirdUserCategories() {
        return thirdUserCategories;
    }

    public void setThirdUserCategories(
            List<UserCategorySummary> thirdUserCategories) {
        this.thirdUserCategories = thirdUserCategories;
    }

    public UserCategorySummary getThirdUserCategory() {
        return thirdUserCategory;
    }

    public void setThirdUserCategory(UserCategorySummary thirdUserCategory) {
        this.thirdUserCategory = thirdUserCategory;
    }

    public UserCategorySummary getSecondUserCategory() {
        return secondUserCategory;
    }

    public void setSecondUserCategory(UserCategorySummary secondUserCategory) {
        this.secondUserCategory = secondUserCategory;
    }
}
