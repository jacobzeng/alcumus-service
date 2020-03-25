package org.fangzz.alcumus.alcumusservice.dto;

import java.util.List;
import java.util.Map;

public class StudentReport3 {
    private UserCategorySummary firstUserCategory;
    private List<UserCategorySummary> secondUserCategories;
    private Map<String, Integer> exerciseLogStats;
    private Map<String, Integer> thirdCategoryStats;

    public UserCategorySummary getFirstUserCategory() {
        return firstUserCategory;
    }

    public void setFirstUserCategory(UserCategorySummary firstUserCategory) {
        this.firstUserCategory = firstUserCategory;
    }

    public List<UserCategorySummary> getSecondUserCategories() {
        return secondUserCategories;
    }

    public void setSecondUserCategories(
            List<UserCategorySummary> secondUserCategories) {
        this.secondUserCategories = secondUserCategories;
    }

    public Map<String, Integer> getExerciseLogStats() {
        return exerciseLogStats;
    }

    public void setExerciseLogStats(Map<String, Integer> exerciseLogStats) {
        this.exerciseLogStats = exerciseLogStats;
    }

    public Map<String, Integer> getThirdCategoryStats() {
        return thirdCategoryStats;
    }

    public void setThirdCategoryStats(Map<String, Integer> thirdCategoryStats) {
        this.thirdCategoryStats = thirdCategoryStats;
    }
}
