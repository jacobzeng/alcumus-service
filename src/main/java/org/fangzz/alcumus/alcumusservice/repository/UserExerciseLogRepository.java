package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserExerciseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserExerciseLogRepository extends AbstractRepository<UserExerciseLog> {
    UserExerciseLog findByUserAndCategoryAndStatus(User currentUser, ExerciseCategory category, int statusCurrent);

    UserExerciseLog findByUserAndExercise(User student, Exercise exercise);

    int countByUserAndStatus(User student, int status);

    Page<UserExerciseLog> findByUser(User user, Pageable pageRequest);

    List<UserExerciseLog> findByUserAndCategoryAndStatusIn(User student, ExerciseCategory category, int[] statusIn,
                                                           Sort sort);

    List<UserExerciseLog> findByUserAndCategoryCodeLikeAndStatusIn(User student, String categoryCode, int[] statusIn,
                                                                   Sort sort);

    Integer countByUserAndCategoryCodeLikeAndStatus(User student, String categoryCodeLike, int status);
}
