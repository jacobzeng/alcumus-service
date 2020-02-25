package org.fangzz.alcumus.alcumusservice.dto.param;

import javax.validation.constraints.NotEmpty;

public class ExerciseCategoryCreateParameter {

    @NotEmpty(message = "分类名称不能为空")
    private String name;

    private Integer parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
