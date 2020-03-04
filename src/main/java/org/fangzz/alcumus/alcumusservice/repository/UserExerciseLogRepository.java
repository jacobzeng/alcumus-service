package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserExerciseLog;

public interface UserExerciseLogRepository extends AbstractRepository<UserExerciseLog> {
    UserExerciseLog findByUserAndCategoryAndStatus(User currentUser, ExerciseCategory category, int statusCurrent);

    UserExerciseLog findByUserAndExercise(User student, Exercise exercise);
}
