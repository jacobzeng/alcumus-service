package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExerciseRepository extends AbstractRepository<Exercise> {

    Exercise findFirstByCategoryAndDeleted(ExerciseCategory category, boolean b, Sort difficulty);

    Exercise findFirstByDeletedOrderByDifficultyAsc(boolean b);

    long countByCategoryAndDeleted(ExerciseCategory category, boolean b);

    long countByDeleted(boolean b);

    Page<Exercise> findByCategory(ExerciseCategory category, Pageable pageRequest);

    @Query(value = "select a from Exercise a where a.category=:category and a.online=true and a.difficulty>:minDifficulty and a.difficulty<=:maxDifficulty and a.id not in(select b.exercise.id from UserExerciseLog b where b.user=:user)")
    List<Exercise> nextStudentExercises(@Param("category") ExerciseCategory category, @Param("user") User user,
                                        @Param("minDifficulty") BigDecimal minDifficulty,
                                        @Param("maxDifficulty") BigDecimal maxDifficulty,
                                        Pageable pageable);

    @Query(value = "select count(a.id) from Exercise a where a.category=:category and a.online=true and a.difficulty>:minDifficulty and a.difficulty<=:maxDifficulty and a.id not in(select b.exercise.id from UserExerciseLog b where b.user=:user)")
    int countNextStudentExercises(@Param("category") ExerciseCategory category, @Param("user") User user,
                                  @Param("minDifficulty") BigDecimal minDifficulty,
                                  @Param("maxDifficulty") BigDecimal maxDifficulty);
}
