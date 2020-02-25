package org.fangzz.alcumus.alcumusservice.web.mgr;

import org.fangzz.alcumus.alcumusservice.dto.ExerciseCategorySummary;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryCreateParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryQueryParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryUpdateParameter;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
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
}
