package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategoryScoreDefinition;

import java.util.List;

public interface ExerciseCategoryScoreDefinitionRepository extends AbstractRepository<ExerciseCategoryScoreDefinition> {
    ExerciseCategoryScoreDefinition findByCategoryAndExerciseAndStatus(ExerciseCategory category, Exercise exercise, int status);

    List<ExerciseCategoryScoreDefinition> findByExercise(Exercise exercise);

    List<ExerciseCategoryScoreDefinition> findByExerciseAndStatus(Exercise exercise, int status);
}
