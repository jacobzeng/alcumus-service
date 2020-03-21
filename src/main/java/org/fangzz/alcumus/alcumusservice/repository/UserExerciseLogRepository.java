package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserExerciseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserExerciseLogRepository extends AbstractRepository<UserExerciseLog> {
    UserExerciseLog findByUserAndCategoryAndStatus(User currentUser, ExerciseCategory category, int statusCurrent);

    UserExerciseLog findByUserAndExercise(User student, Exercise exercise);

    int countByUserAndStatus(User student, int status);

    Page<UserExerciseLog> findByUser(User user, Pageable pageRequest);
}
