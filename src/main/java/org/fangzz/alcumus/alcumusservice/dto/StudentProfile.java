package org.fangzz.alcumus.alcumusservice.dto;

import java.util.List;

public class StudentProfile {
    private UserCategorySummary rootUserCategory;
    private List<UserCategorySummary> topUserCategories;

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
