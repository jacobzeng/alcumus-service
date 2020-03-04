package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserScore;

public interface UserScoreRepository extends AbstractRepository<UserScore> {
    UserScore findByUserAndCategory(User student, ExerciseCategory category);
}
