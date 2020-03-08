package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;

public interface ExerciseCategoryRepository extends AbstractRepository<ExerciseCategory> {
    ExerciseCategory findByName(String name);
}
