package org.fangzz.alcumus.alcumusservice.dto;

import java.util.List;
import java.util.Map;

public class StudentProfile {
    private UserCategorySummary rootUserCategory;
    private List<UserCategorySummary> topUserCategories;
    private Map<String, Integer> exerciseLogStats;
    private Map<Integer, Integer> thirdCategoryStats;

    public Map<String, Integer> getExerciseLogStats() {
        return exerciseLogStats;
    }

    public void setExerciseLogStats(Map<String, Integer> exerciseLogStats) {
        this.exerciseLogStats = exerciseLogStats;
    }

    public Map<Integer, Integer> getThirdCategoryStats() {
        return thirdCategoryStats;
    }

    public void setThirdCategoryStats(Map<Integer, Integer> thirdCategoryStats) {
        this.thirdCategoryStats = thirdCategoryStats;
    }

    public UserCategorySummary getRootUserCategory() {
        return rootUserCategory;
    }

    public void setRootUserCategory(UserCategorySummary rootUserCategory) {
        this.rootUserCategory = rootUserCategory;
    }

    public List<UserCategorySummary> getTopUserCategories() {
        return topUserCategories;
    }

    public void setTopUserCategories(List<UserCategorySummary> topUserCategories) {
        this.topUserCategories = topUserCategories;
    }
}
