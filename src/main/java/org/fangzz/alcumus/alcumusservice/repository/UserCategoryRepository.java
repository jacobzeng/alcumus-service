package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCategoryRepository extends AbstractRepository<UserCategory> {
    UserCategory findByUserAndCurrent(User student, boolean current);

    UserCategory findByUserAndCategory(User currentUser, ExerciseCategory category);

    UserCategory findTop1ByUserAndCategoryParentAndScoreLessThanAndIdNot(User student, ExerciseCategory secondCategory,
                                                                         int score, Integer id);

    @Query(value = "SELECT avg(a.score) FROM UserCategory a where a.category.parent=:category and a.user=:user")
    Double avgScore(@Param("category") ExerciseCategory category, @Param("user") User user);

    @Query(value = "SELECT avg(a.score) FROM UserCategory a where a.category.level=1 and a.user=:user")
    Double avgRootScore(@Param("user") User user);

    @Query(value = "SELECT avg(a.difficultyLevel) FROM UserCategory a where a.category.parent=:category and a.user=:user")
    Double avgDifficultyLevel(@Param("category") ExerciseCategory category, @Param("user") User user);

    @Query(value = "SELECT avg(a.difficultyLevel) FROM UserCategory a where a.category.level=1 and a.user=:user")
    Double avgRootDifficultyLevel(@Param("user") User user);

    @Query(value = "SELECT avg(a.userLevel) FROM UserCategory a where a.category.parent=:category and a.user=:user")
    Double avgUserLevel(@Param("category") ExerciseCategory category, @Param("user") User user);

    @Query(value = "SELECT avg(a.userLevel) FROM UserCategory a where a.category.level=1 and a.user=:user")
    Double avgRootUserLevel(@Param("user") User user);

    Page<UserCategory> findByUserAndCategoryLevel(User student, int categoryLevel, Pageable queryUserCategory);
}
