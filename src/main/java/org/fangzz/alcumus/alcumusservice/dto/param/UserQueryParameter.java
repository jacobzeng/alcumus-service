package org.fangzz.alcumus.alcumusservice.dto.param;

import org.springframework.data.domain.Sort;

public class UserQueryParameter extends PageQueryParameter {
    private String usernameLike;

    public String getUsernameLike() {
        return usernameLike;
    }

    public void setUsernameLike(String usernameLike) {
        this.usernameLike = usernameLike;
    }

    @Override
    public Sort genSort() {
        Sort sort = super.genSort();
        if (null == sort) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        return sort;
    }
}
