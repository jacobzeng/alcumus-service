package org.fangzz.alcumus.alcumusservice.web.mgr;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import org.fangzz.alcumus.alcumusservice.dto.ExerciseCategoryScoreDefinitionSummary;
import org.fangzz.alcumus.alcumusservice.dto.ExerciseCategorySummary;
import org.fangzz.alcumus.alcumusservice.dto.ExerciseSummary;
import org.fangzz.alcumus.alcumusservice.dto.param.*;
import org.fangzz.alcumus.alcumusservice.model.Exercise;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.ExerciseCategoryScoreDefinition;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/mgr/exercise-categories/query")
    @Transactional(readOnly = true)
    public Page<ExerciseCategorySummary> queryExerciseCategories(ExerciseCategoryQueryParameter2 parameter) {
        Page<ExerciseCategory> queryResult = exerciseService.queryExerciseCategories(parameter, requireUser());
        return new PageImpl<ExerciseCategorySummary>(
                queryResult.getContent().stream().map(ExerciseCategorySummary::from).collect(Collectors.toList()),
                queryResult.getPageable(),
                queryResult.getTotalElements()
        );
    }

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

    @PostMapping("/mgr/exercises/{id}/score-definitions")
    public ExerciseCategoryScoreDefinitionSummary createExerciseCategoryScoreDefinition(@PathVariable Integer id,
                                                                                        @RequestBody ExerciseCategoryScoreDefinitionCreateParameter parameter) {
        return ExerciseCategoryScoreDefinitionSummary
                .from(exerciseService.createExerciseCategoryScoreDefinition(id, parameter, requireUser()));
    }

    @PostMapping("/mgr/exercise-score-definitions/{id}")
    public ExerciseCategoryScoreDefinitionSummary updateExerciseCategoryScoreDefinition(@PathVariable Integer id,
                                                                                        @RequestBody ExerciseCategoryScoreDefinitionUpdateParameter parameter) {
        return ExerciseCategoryScoreDefinitionSummary
                .from(exerciseService.updateExerciseCategoryScoreDefinition(id, parameter, requireUser()));
    }

    @DeleteMapping("/mgr/exercise-score-definitions/{id}")
    public void deleteExerciseCategoryScoreDefinition(@PathVariable Integer id) {
        exerciseService.deleteExerciseCategoryScoreDefinition(id, requireUser());
    }

    @GetMapping("/mgr/exercises/{id}/score-definitions")
    @Transactional(readOnly = true)
    public List<ExerciseCategoryScoreDefinitionSummary> listScoreDefinitions(@PathVariable Integer id) {
        List<ExerciseCategoryScoreDefinition> definitions = exerciseService.listScoreDefinitions(id, requireUser());
        return definitions.stream().map(ExerciseCategoryScoreDefinitionSummary::from).collect(Collectors.toList());
    }

    @PostMapping("/mgr/import/exercises")
    public Map importExercises(@RequestParam("file") MultipartFile file,
                               @RequestParam("parentCategory") String parentCategoryName) throws IOException {
        Map result = Maps.newHashMap();
        if (file.isEmpty()) {
            return result;
        }

        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return file.getInputStream();
            }
        };

        ExerciseCategory parentCategory = exerciseService.findExerciseCategoryByName(parentCategoryName);
        List<String> lines = byteSource.asCharSource(Charsets.UTF_8).readLines();
        String secondCategoryName = null;
        ExerciseCategory secondCategory = null;
        String thirdCategoryName = null;
        ExerciseCategory thirdCategory = null;

        String exerciseName = null;
        String exerciseDesc = null;
        String exerciseAnswer = null;
        String exerciseAnswerDesc = null;
        BigDecimal exerciseDifficulty = null;
        boolean exerciseEnd = true;

        for (String line : lines) {
            line = line.trim();
            if (Strings.isNullOrEmpty(line)) {
                continue;
            }
            if (line.startsWith("标题") || line.startsWith("题目") || line.startsWith("答案") || line.startsWith("解析")
                    || line.startsWith("难度")) {
                if (line.startsWith("标题：")) {
                    exerciseEnd = false;
                    exerciseName = line.split("标题：")[1];
                } else if (line.startsWith("题目：")) {
                    exerciseDesc = line.split("题目：")[1];
                } else if (line.startsWith("答案：")) {
                    exerciseAnswer = line.split("答案：")[1];
                } else if (line.startsWith("解析：")) {
                    exerciseAnswerDesc = line.split("解析：")[1];
                } else if (line.startsWith("难度：")) {
                    exerciseDifficulty = new BigDecimal(line.split("难度：")[1]);

                    ExerciseCreateParameter exerciseCreateParameter = new ExerciseCreateParameter();
                    exerciseCreateParameter.setCategoryId(thirdCategory.getId());
                    exerciseCreateParameter.setOnline(true);
                    exerciseCreateParameter.setName(exerciseName);
                    exerciseCreateParameter.setDesc(exerciseDesc);
                    exerciseCreateParameter.setAnswer(exerciseAnswer);
                    exerciseCreateParameter.setAnswerDesc(exerciseAnswerDesc);
                    exerciseCreateParameter.setDifficulty(exerciseDifficulty);

                    exerciseService.createExercise(exerciseCreateParameter, requireUser());
                    exerciseEnd = true;
                    exerciseName = null;
                    exerciseDesc = null;
                    exerciseAnswer = null;
                    exerciseAnswerDesc = null;
                    exerciseDifficulty = null;

                }

            } else if (!exerciseEnd) {
                //一道题还未结束，那就是有内容换行了
                if (exerciseDifficulty == null) {
                    //还未设置难度,上一行是问题答案解析
                    exerciseAnswerDesc += "\\r\\n" + line;
                }

            } else {


                if (secondCategoryName == null) {
                    secondCategoryName = line;
                    secondCategory = exerciseService.createExerciseCategoryIfNotExist(secondCategoryName,
                            parentCategory, requireUser());

                } else {
                    String categoryIndex = line.split("\\.")[0];
                    String currentCategoryIndex = secondCategoryName.split("\\.")[0];
                    if (!categoryIndex.equals(currentCategoryIndex)) {
                        //换大分类了
                        secondCategoryName = line;
                        secondCategory = exerciseService.createExerciseCategoryIfNotExist(secondCategoryName,
                                parentCategory, requireUser());
                    } else {
                        thirdCategoryName = line;
                        thirdCategory = exerciseService.createExerciseCategoryIfNotExist(thirdCategoryName,
                                secondCategory, requireUser());
                    }


                }
            }
        }

        return result;
    }
}
