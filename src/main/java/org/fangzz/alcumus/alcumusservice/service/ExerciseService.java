package org.fangzz.alcumus.alcumusservice.service;

import org.fangzz.alcumus.alcumusservice.dto.param.*;
import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface ExerciseService {
    List<ExerciseCategory> listExerciseCategories(@NotNull ExerciseCategoryQueryParameter parameter,
                                                  @NotNull User currentUser);

    ExerciseCategory createExerciseCategory(@NotNull @Valid ExerciseCategoryCreateParameter parameter,
                                            @NotNull User requireUser);

    ExerciseCategory findExerciseCategoryById(int id, @NotNull User requireUser);

    ExerciseCategory updateExerciseCategory(@NotNull Integer id,
                                            @NotNull @Valid ExerciseCategoryUpdateParameter parameter,
                                            @NotNull User requireUser);

    void deleteExerciseCategory(@NotNull Integer id, @NotNull User requireUser);

    Exercise createExercise(@NotNull @Valid ExerciseCreateParameter parameter, @NotNull User requireUser);

    Exercise updateExercise(@NotNull Integer id, @NotNull @Valid ExerciseCreateParameter parameter,
                            @NotNull User requireUser);

    void deleteExercise(@NotNull Integer id, @NotNull User requireUser);

    Page<Exercise> queryExercises(@NotNull ExerciseQueryParameter parameter, @NotNull User requireUser);
}
