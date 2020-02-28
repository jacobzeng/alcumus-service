package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ExerciseRepository extends AbstractRepository<Exercise> {

    Exercise findFirstByCategoryAndDeleted(ExerciseCategory category, boolean b, Sort difficulty);

    Exercise findFirstByDeletedOrderByDifficultyAsc(boolean b);

    long countByCategoryAndDeleted(ExerciseCategory category, boolean b);

    long countByDeleted(boolean b);

    Page<Exercise> findByCategory(ExerciseCategory category, Pageable pageRequest);
}
