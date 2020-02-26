package org.fangzz.alcumus.alcumusservice.dto.param;

import org.springframework.data.domain.Sort;

public class ExerciseQueryParameter extends PageQueryParameter {
    private Integer categoryId;
    private String nameLike;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    @Override
    public Sort genSort() {
        Sort sort = super.genSort();
        if (null == sort) {
            sort = Sort.by(Sort.Direction.ASC, "name", "difficulty");
        }
        return sort;
    }
}
