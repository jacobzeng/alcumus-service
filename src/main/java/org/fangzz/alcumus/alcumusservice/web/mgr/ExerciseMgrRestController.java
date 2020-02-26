package org.fangzz.alcumus.alcumusservice.web.mgr;

import org.fangzz.alcumus.alcumusservice.dto.ExerciseCategorySummary;
import org.fangzz.alcumus.alcumusservice.dto.ExerciseSummary;
import org.fangzz.alcumus.alcumusservice.dto.param.*;
import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 练习题管理的rest controller
 */

@RestController
@RequestMapping("/api")
@Transactional
public class ExerciseMgrRestController extends UserAwareController {
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/mgr/exercise-categories")
    @Transactional(readOnly = true)
    public List<ExerciseCategorySummary> listExerciseCategories(ExerciseCategoryQueryParameter parameter) {
        List<ExerciseCategory> queryResult = exerciseService.listExerciseCategories(parameter, requireUser());
        return queryResult.stream().map(ExerciseCategorySummary::from).collect(Collectors.toList());
    }

    @PostMapping("/mgr/exercise-categories")
    public ExerciseCategorySummary createExerciseCategory(@RequestBody ExerciseCategoryCreateParameter parameter) {
        return ExerciseCategorySummary.from(exerciseService.createExerciseCategory(parameter, requireUser()));
    }

    @PostMapping("/mgr/exercise-categories/{id}")
    public ExerciseCategorySummary updateExerciseCategory(@PathVariable Integer id,
                                                          @RequestBody ExerciseCategoryUpdateParameter parameter) {
        return ExerciseCategorySummary.from(exerciseService.updateExerciseCategory(id, parameter, requireUser()));
    }

    @DeleteMapping("/mgr/exercise-categories/{id}")
    public void deleteExerciseCategory(@PathVariable Integer id) {
        exerciseService.deleteExerciseCategory(id, requireUser());
    }

    @PostMapping("/mgr/exercises")
    public ExerciseSummary createExercise(@RequestBody ExerciseCreateParameter parameter) {
        return ExerciseSummary.from(exerciseService.createExercise(parameter, requireUser()));
    }

    @PostMapping("/mgr/exercises/{id}")
    public ExerciseSummary updateExercise(@PathVariable Integer id, @RequestBody ExerciseCreateParameter parameter) {
        return ExerciseSummary.from(exerciseService.updateExercise(id, parameter, requireUser()));
    }

    @DeleteMapping("/mgr/exercises/{id}")
    public void deleteExercise(@PathVariable Integer id) {
        exerciseService.deleteExercise(id, requireUser());
    }

    @GetMapping("/mgr/exercises")
    @Transactional(readOnly = true)
    public Page<ExerciseSummary> queryExercises(ExerciseQueryParameter parameter) {
        Page<Exercise> queryResult = exerciseService.queryExercises(parameter, requireUser());
        return new PageImpl<>(queryResult.stream().map(ExerciseSummary::from).collect(Collectors.toList()),
                queryResult.getPageable(),
                queryResult.getTotalElements()
        );
    }
}
