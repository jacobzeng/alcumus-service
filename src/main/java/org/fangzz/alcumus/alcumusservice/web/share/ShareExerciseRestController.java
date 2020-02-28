package org.fangzz.alcumus.alcumusservice.web.share;

import org.fangzz.alcumus.alcumusservice.dto.ExerciseCategorySummary;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseCategoryQueryParameter;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping("/api")
public class ShareExerciseRestController {
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/share/exercise-categories")
    public List<ExerciseCategorySummary> listCategories(ExerciseCategoryQueryParameter parameter) {
        List<ExerciseCategory> queryResult = exerciseService.listExerciseCategories(parameter);
        return queryResult.stream().map(ExerciseCategorySummary::from).collect(Collectors.toList());
    }
}
