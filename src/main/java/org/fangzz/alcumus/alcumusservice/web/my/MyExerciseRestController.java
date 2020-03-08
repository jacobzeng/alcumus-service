package org.fangzz.alcumus.alcumusservice.web.my;

import org.fangzz.alcumus.alcumusservice.dto.*;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseAnswerParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.ExerciseGiveUpParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.StudentSetCurrentCategoryParameter;
import org.fangzz.alcumus.alcumusservice.model.UserCategory;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping("/api")
public class MyExerciseRestController extends UserAwareController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/my/exercise-categories/focus")
    @Transactional(readOnly = true)
    public ExerciseCategorySummary currentFocus() {
        UserCategory userCategory = exerciseService.getStudentCurrentCategory(currentUser());
        if (null == userCategory) {
            return null;
        }
        return ExerciseCategorySummary.from(userCategory.getCategory());
    }

    @PostMapping("/my/exercise-categories/focus")
    public BaseDto setCurrentFocus(@RequestBody StudentSetCurrentCategoryParameter parameter) {
        UserCategory model = exerciseService.setStudentCurrentCategory(parameter, currentUser());
        BaseDto result = new BaseDto();
        BaseDto.convert(model, result);
        return result;
    }

    @GetMapping("/my/exercises/next")
    @Transactional(readOnly = true)
    public ExerciseSummary nextExercise() {
        return ExerciseSummary.from(exerciseService.nextStudentExercise(currentUser()));
    }

    @PostMapping("/my/exercises/answer")
    public ExerciseAnswerResponse submitAnswer(@RequestBody ExerciseAnswerParameter parameter) {
        parameter.setAnswer(parameter.getAnswer().trim());
        return exerciseService.submitStudentAnswer(currentUser(), parameter);
    }

    @PostMapping("/my/exercises/give-up")
    public ExerciseGiveUpResponse giveUpExercise(@RequestBody ExerciseGiveUpParameter parameter) {
        return exerciseService.giveUpExercise(currentUser(), parameter);
    }
}
