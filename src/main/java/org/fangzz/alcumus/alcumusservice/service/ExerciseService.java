package org.fangzz.alcumus.alcumusservice.service;

import org.fangzz.alcumus.alcumusservice.dto.*;
import org.fangzz.alcumus.alcumusservice.dto.param.*;
import org.fangzz.alcumus.alcumusservice.model.*;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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

    UserCategory getStudentCurrentCategory(@NotNull User student);

    List<ExerciseCategory> listExerciseCategories(@NotNull ExerciseCategoryQueryParameter parameter);

    UserCategory setStudentCurrentCategory(@NotNull @Valid StudentSetCurrentCategoryParameter parameter,
                                           @NotNull User currentUser);

    Exercise nextStudentExercise(@NotNull User currentUser);

    ExerciseAnswerResponse submitStudentAnswer(@NotNull User student,
                                               @NotNull @Valid ExerciseAnswerParameter parameter);

    ExerciseGiveUpResponse giveUpExercise(@NotNull User student, @NotNull @Valid ExerciseGiveUpParameter parameter);

    ExerciseCategoryScoreDefinition createExerciseCategoryScoreDefinition(@NotNull Integer id,
                                                                          @NotNull @Valid ExerciseCategoryScoreDefinitionCreateParameter parameter,
                                                                          @NotNull User requireUser);

    ExerciseCategoryScoreDefinition updateExerciseCategoryScoreDefinition(@NotNull Integer id,
                                                                          @NotNull @Valid ExerciseCategoryScoreDefinitionUpdateParameter parameter,
                                                                          @NotNull User requireUser);

    void deleteExerciseCategoryScoreDefinition(@NotNull Integer id, @NotNull User requireUser);

    List<ExerciseCategoryScoreDefinition> listScoreDefinitions(@NotNull Integer id, @NotNull User requireUser);

    Page<ExerciseCategory> queryExerciseCategories(@NotNull ExerciseCategoryQueryParameter2 parameter,
                                                   @NotNull User requireUser);

    List<UserScore> listUserScores(@NotNull UserScoreListParameter parameter, @NotNull User user);

    ExerciseCategory findExerciseCategoryByName(@NotEmpty String name);


    ExerciseCategory createExerciseCategoryIfNotExist(@NotEmpty String name, ExerciseCategory parent,
                                                      @NotNull User currentUser);

    List<UserCategory> listUserCategories(UserCategoryQueryParameter parameter);

    StudentProfile getStudentProfile(@NotNull User student);

    void calculateUserExerciseLogStats(@NotNull Integer id, @NotNull User requireUser);

    StudentReport1 getStudentReport1(@NotNull Integer categoryId, @NotNull User student);

    StudentReport2 getStudentReport2(@NotNull Integer categoryId, @NotNull User currentUser);

    StudentReport3 getMyStudentReport3(@NotNull Integer categoryId, @NotNull User currentUser);

    UserExerciseLog getUserExerciseLogById(@NotNull Integer id, @NotNull User currentUser);
}
