package org.fangzz.alcumus.alcumusservice.service;

import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryCreateParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryQueryParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryUpdateParameter;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;

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
}
