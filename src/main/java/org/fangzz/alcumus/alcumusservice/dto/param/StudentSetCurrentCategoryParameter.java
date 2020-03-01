package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotNull;

public class StudentSetCurrentCategoryParameter {
    @NotNull(message = "请选择一个习题分类")
    private Integer categoryId;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
