package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserCategory;

public interface UserCategoryRepository extends AbstractRepository<UserCategory> {
    UserCategory findByUserAndCurrent(User student, boolean current);

    UserCategory findByUserAndCategory(User currentUser, ExerciseCategory category);
}
