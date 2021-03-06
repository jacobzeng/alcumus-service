package org.fangzz.alcumus.alcumusservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fangzz.alcumus.alcumusservice.Constants;
import org.fangzz.alcumus.alcumusservice.dto.*;
import org.fangzz.alcumus.alcumusservice.dto.param.*;
import org.fangzz.alcumus.alcumusservice.exception.BizException;
import org.fangzz.alcumus.alcumusservice.exception.ResourceNotFoundException;
import org.fangzz.alcumus.alcumusservice.model.*;
import org.fangzz.alcumus.alcumusservice.repository.*;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.service.UserActivityService;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static org.fangzz.alcumus.alcumusservice.Constants.START_CATEGORY_SCORE;

@Validated
@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final static Log log = LogFactory.getLog(ExerciseServiceImpl.class);

    @Autowired
    private ExerciseCategoryRepository exerciseCategoryRepository;

    @Autowired
    private ExerciseTagRepository exerciseTagRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private UserExerciseLogRepository userExerciseLogRepository;

    @Autowired
    private ExerciseCategoryScoreDefinitionRepository exerciseCategoryScoreDefinitionRepository;

    @Autowired
    private UserScoreRepository userScoreRepository;

    @Autowired
    private UserExerciseLogStatRepository userExerciseLogStatRepository;

    @Autowired
    private UserService userService;

    private Random random = new Random();

    @Override
    public List<ExerciseCategory> listExerciseCategories(@NotNull ExerciseCategoryQueryParameter parameter,
                                                         @NotNull User currentUser) {
        return exerciseCategoryRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.isFalse(root.get("deleted")));
                if (null != parameter.getParentId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("parent").get("id"), parameter.getParentId()));
                } else {
                    predicateList.add(criteriaBuilder.isNull(root.get("parent")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    @Override
    public ExerciseCategory createExerciseCategory(@NotNull @Valid ExerciseCategoryCreateParameter parameter,
                                                   @NotNull User requireUser) {
        log.info(String.format("%s(%d) create exercise category %s", requireUser.getUsername(), requireUser.getId(),
                parameter.getName()));

        ExerciseCategory model = new ExerciseCategory();
        model.setName(parameter.getName());
        ExerciseCategory parent = null;
        if (null != parameter.getParentId()) {
            parent = findExerciseCategoryById(parameter.getParentId(), requireUser);
        }

        if (null != parent) {
            model.setParent(parent);
            model.setLevel(parent.getLevel() + 1);
        } else {
            model.setLevel(0);
        }
        model = exerciseCategoryRepository.save(model);

        //更新code
        if (null == parent) {
            model.setCode("" + model.getId());
        } else {
            model.setCode(parent.getCode() + model.getId());
        }

        return exerciseCategoryRepository.save(model);
    }

    @Override
    public ExerciseCategory findExerciseCategoryById(int id, @NotNull User requireUser) {
        ExerciseCategory existed = exerciseCategoryRepository.findById(id).orElse(null);
        if (null == existed) {
            throw new ResourceNotFoundException();
        }
        if (existed.isDeleted()) {
            throw new ResourceNotFoundException();
        }
        return existed;
    }

    @Override
    public ExerciseCategory updateExerciseCategory(@NotNull Integer id,
                                                   @NotNull @Valid ExerciseCategoryUpdateParameter parameter,
                                                   @NotNull User requireUser) {
        ExerciseCategory existed = findExerciseCategoryById(id, requireUser);
        existed.setName(parameter.getName());
        return exerciseCategoryRepository.save(existed);
    }

    @Override
    public void deleteExerciseCategory(@NotNull Integer id, @NotNull User requireUser) {
        ExerciseCategory existed = findExerciseCategoryById(id, requireUser);
        existed.setDeleted(true);
        exerciseCategoryRepository.save(existed);
    }

    @Override
    public Exercise createExercise(@NotNull @Valid ExerciseCreateParameter parameter, @NotNull User requireUser) {
        log.info(String.format("%s(%d) create exercise %s", requireUser.getUsername(), requireUser.getId(),
                parameter.getName()));
        ExerciseCategory category = null;
        if (null != parameter.getCategoryId()) {
            category = findExerciseCategoryById(parameter.getCategoryId(), requireUser);
            if (category.getLevel() != 2) {
                throw new BizException("在最底层专题才能添加习题");
            }
        }

        ExerciseCategory secondCategory = null;
        if (null != parameter.getSecondCategoryId()) {
            secondCategory = findExerciseCategoryById(parameter.getSecondCategoryId(), requireUser);
            if (secondCategory.getLevel() != 2) {
                throw new BizException("在最底层专题才能添加习题");
            }
        }

        Exercise model = new Exercise();
        model.setCategory(category);
        model.setSecondCategory(secondCategory);
        BeanUtils.copyProperties(parameter, model);
        if (null != parameter.getTags() && parameter.getTags().length > 0) {
            List<String> tagNames = Lists.newArrayList(parameter.getTags());
            tagNames.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });

            for (String tagName : tagNames) {
                model.getTags().add(createTagIfNotExisted(tagName));
            }

            model.setTagNames(String.join(",", tagNames));
        }

        return exerciseRepository.save(model);
    }

    @Override
    public Exercise updateExercise(@NotNull Integer id, @NotNull @Valid ExerciseCreateParameter parameter,
                                   @NotNull User requireUser) {
        log.info(String.format("%s(%d) update exercise %d", requireUser.getUsername(), requireUser.getId(),
                id));
        Exercise existed = findExerciseById(id, requireUser);
        BeanUtils.copyProperties(parameter, existed);
        if (null != parameter.getTags() && parameter.getTags().length > 0) {
            List<String> tagNames = Lists.newArrayList(parameter.getTags());
            tagNames.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });

            for (String tagName : tagNames) {
                existed.getTags().add(createTagIfNotExisted(tagName));
            }

            existed.setTagNames(String.join(",", tagNames));
        } else {
            existed.getTags().clear();
            existed.setTagNames(null);
        }
        return exerciseRepository.save(existed);
    }

    @Override
    public void deleteExercise(@NotNull Integer id, @NotNull User requireUser) {
        Exercise exercise = findExerciseById(id, requireUser);
        exercise.setDeleted(true);
        exerciseRepository.save(exercise);
    }

    @Override
    public Page<Exercise> queryExercises(@NotNull ExerciseQueryParameter parameter, @NotNull User requireUser) {
        PageRequest pageable = PageRequest.of(parameter.getStart(), parameter.getLimit(), parameter.genSort());
        return exerciseRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.equal(root.get("deleted"), false));

                if (!StringUtils.isEmpty(parameter.getNameLike())) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%" + parameter.getNameLike() + "%"));
                }

                if (null != parameter.getCategoryId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("category").get("id"), parameter.getCategoryId()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, pageable);
    }

    @Override
    public UserCategory getStudentCurrentCategory(@NotNull User student) {
        UserCategory existed = userCategoryRepository.findByUserAndCurrent(student, true);
        if (existed != null) {
            return existed;
        } else {
            return null;
        }
    }

    @Override
    public List<ExerciseCategory> listExerciseCategories(@NotNull ExerciseCategoryQueryParameter parameter) {
        return exerciseCategoryRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.isFalse(root.get("deleted")));
                if (null != parameter.getParentId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("parent").get("id"), parameter.getParentId()));
                } else {
                    predicateList.add(criteriaBuilder.isNull(root.get("parent")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    public void cleanStudentCurrentCategory(@NotNull User student) {
        UserCategory current = userCategoryRepository.findByUserAndCurrent(student, true);
        if (null != current) {
            current.setCurrent(false);
            userCategoryRepository.save(current);
        }
    }

    @Override
    public UserCategory setStudentCurrentCategory(@NotNull @Valid StudentSetCurrentCategoryParameter parameter,
                                                  @NotNull User currentUser) {

        UserCategory current = userCategoryRepository.findByUserAndCurrent(currentUser, true);
        if (null != current) {
            current.setCurrent(false);
            userCategoryRepository.save(current);
        }

        ExerciseCategory category = findExerciseCategoryById(parameter.getCategoryId());
        if (category.getLevel() != 2) {
            throw new BizException("您只能选择最底层分类");
        }
        UserCategory existed = userCategoryRepository.findByUserAndCategory(currentUser, category);
        if (null == existed) {
            existed = initUserCategory(currentUser, category);
        }
        existed.setCurrent(true);
        existed = userCategoryRepository.save(existed);
        return existed;
    }

    @Override
    public Exercise nextStudentExercise(@NotNull User currentUser) {
        UserCategory userCategory = getStudentCurrentCategory(currentUser);
        if (null == userCategory) {
            throw new BizException("请您先选择一个分类");
        }
        ExerciseCategory category = userCategory.getCategory();

        //1. 查找当前有没有正在做的题
        UserExerciseLog log = userExerciseLogRepository
                .findByUserAndCategoryAndStatus(currentUser, category, UserExerciseLog.STATUS_CURRENT);
        if (null != log) {
            return log.getExercise();
        }

        //2. 选择一道题目
        int difficultyLevel = userCategory.getDifficultyLevel();
        List<Exercise> exercises = Lists.newArrayList();
        while (exercises.isEmpty()) {
            BigDecimal maxDifficulty = new BigDecimal(new Double(1.0).toString());
            BigDecimal minDifficulty = new BigDecimal(new Double(0.9).toString());
            switch (difficultyLevel) {
                case 2:
                    maxDifficulty = new BigDecimal(new Double(0.9).toString());
                    minDifficulty = new BigDecimal(new Double(0.8).toString());
                    break;
                case 3:
                    maxDifficulty = new BigDecimal(new Double(0.8).toString());
                    minDifficulty = new BigDecimal(new Double(0.7).toString());
                    break;
                case 4:
                    maxDifficulty = new BigDecimal(new Double(0.7).toString());
                    minDifficulty = new BigDecimal(new Double(0.6).toString());
                    break;
                case 5:
                    maxDifficulty = new BigDecimal(new Double(0.6).toString());
                    minDifficulty = new BigDecimal(new Double(0.5).toString());
                    break;
                case 6:
                    maxDifficulty = new BigDecimal(new Double(0.5).toString());
                    minDifficulty = new BigDecimal(new Double(0.45).toString());
                    break;
                case 7:
                    maxDifficulty = new BigDecimal(new Double(0.45).toString());
                    minDifficulty = new BigDecimal(new Double(0.40).toString());
                    break;
                case 8:
                    maxDifficulty = new BigDecimal(new Double(0.40).toString());
                    minDifficulty = new BigDecimal(new Double(0.35).toString());
                    break;
                case 9:
                    maxDifficulty = new BigDecimal(new Double(0.35).toString());
                    minDifficulty = new BigDecimal(new Double(0.30).toString());
                    break;
                case 10:
                    maxDifficulty = new BigDecimal(new Double(0.30).toString());
                    minDifficulty = new BigDecimal(new Double(0.25).toString());
                    break;
                case 11:
                    maxDifficulty = new BigDecimal(new Double(0.25).toString());
                    minDifficulty = new BigDecimal(new Double(0.20).toString());
                    break;
                case 12:
                    maxDifficulty = new BigDecimal(new Double(0.20).toString());
                    minDifficulty = new BigDecimal(new Double(0.15).toString());
                    break;
                case 13:
                    maxDifficulty = new BigDecimal(new Double(0.15).toString());
                    minDifficulty = new BigDecimal(new Double(0.10).toString());
                    break;
                case 14:
                    maxDifficulty = new BigDecimal(new Double(0.10).toString());
                    minDifficulty = new BigDecimal(new Double(0.05).toString());
                    break;
                case 15:
                    maxDifficulty = new BigDecimal(new Double(0.05).toString());
                    minDifficulty = new BigDecimal(new Double(0.0).toString());
                    break;
            }
            int maxCount = exerciseRepository
                    .countNextStudentExercises(category, currentUser, minDifficulty, maxDifficulty);
            if (maxCount == 0 && difficultyLevel >= 15) {
                throw new BizException("抱歉，该分类没有更多的练习题了");
            } else if (maxCount > 0) {
                PageRequest pageRequest = PageRequest.of(random.nextInt(maxCount), 1);
                exercises = exerciseRepository
                        .nextStudentExercises(category, currentUser, minDifficulty, maxDifficulty, pageRequest);
            }
            difficultyLevel++;
        }

        Exercise result = exercises.get(0);

        UserExerciseLog newLog = new UserExerciseLog();
        newLog.setCategory(category);
        newLog.setExercise(result);
        newLog.setUser(currentUser);
        newLog.setStatus(UserExerciseLog.STATUS_CURRENT);
        newLog.setCategoryCode(category.getCode());
        userExerciseLogRepository.save(newLog);

        return result;
    }

    @Override
    public ExerciseAnswerResponse submitStudentAnswer(@NotNull User student,
                                                      @NotNull @Valid ExerciseAnswerParameter parameter) {
        Exercise exercise = findExerciseById(parameter.getExerciseId());
        UserExerciseLog log = userExerciseLogRepository.findByUserAndExercise(student, exercise);
        String lastAnswer = null;
        if (1 == parameter.getAnswers().length) {
            //只回答了一次
            lastAnswer = parameter.getAnswers()[0];
            log.setAnswer(lastAnswer);
        } else {
            lastAnswer = parameter.getAnswers()[1];
            log.setAnswer(parameter.getAnswers()[0]);
            log.setAnswer2(parameter.getAnswers()[1]);
        }


        ExerciseAnswerResponse result = new ExerciseAnswerResponse();

        if (!exercise.getAnswer().equals(lastAnswer)) {
            result.setRight(false);

            //2次都错了
            log.setStatus(UserExerciseLog.STATUS_WRONG);
            userActivityService.addActivity(student, String.format("回答错了题目: %s", exercise.getName()));

        } else {
            result.setRight(true);
            userActivityService.addActivity(student, String.format("回答对了题目: %s", exercise.getName()));

            if (parameter.getCounterOfRetry() == 1) {
                //1次答对
                log.setStatus(UserExerciseLog.STATUS_RIGHT_FIRST_TIME);
            } else {
                //第2次答对
                log.setStatus(UserExerciseLog.STATUS_RIGHT_SECOND_TIME);
            }

        }
        userExerciseLogRepository.save(log);
        increaseUserExerciseLogStat(log);
        calculateUserScore(exercise, log.getStatus(), student);
        return result;
    }

    private void increaseUserExerciseLogStat(UserExerciseLog log) {
        Date date = log.getCreatedAt();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int dayInWeek = date.getDay();

        UserExerciseLogStat existed = userExerciseLogStatRepository
                .findByUserAndYearAndMonthAndDay(log.getUser(), year, month, day);
        if (null == existed) {
            existed = new UserExerciseLogStat();
            existed.setCounter(0);
            existed.setUser(log.getUser());
            existed.setYear(year);
            existed.setMonth(month);
            existed.setDay(day);
            existed.setDayInWeek(dayInWeek);
        }

        existed.setCounter(existed.getCounter() + 1);
        userExerciseLogStatRepository.save(existed);
    }

    private void calculateUserScore(Exercise exercise, int status, User student) {

        //升级当前专题的难度抽屉
        UserCategory category = userCategoryRepository.findByUserAndCategory(student, exercise.getCategory());
        switch (status) {
            case UserExerciseLog.STATUS_RIGHT_FIRST_TIME:
                category.setDifficultyLevel(category.getDifficultyLevel() + 2);
                category.setCounterOfFirstRight(category.getCounterOfFirstRight() + 1);
                break;
            case UserExerciseLog.STATUS_RIGHT_SECOND_TIME:
                category.setCounterOfSecondRight(category.getCounterOfSecondRight() + 1);
                category.setDifficultyLevel(category.getDifficultyLevel() + 2);
                break;
            case UserExerciseLog.STATUS_WRONG:
                category.setCounterOfWrong(category.getCounterOfWrong() + 1);
                category.setDifficultyLevel(category.getDifficultyLevel() - 1);
                break;
            case UserExerciseLog.STATUS_GIVE_UP:
                category.setCounterOfGiveup(category.getCounterOfGiveup() + 1);
                break;
        }
        //算分
        if (exercise.getSecondCategory() == null) {
            /**
             * （1）第一次做对一道难度系数为 a 的题目，积分增加=取整 round(10/a)
             * （2）第一次做错第二次做对，积分增加为一次做对的 60%，=取整 round(6/a)
             * （3）一错二错/放弃，积分减少为一次做对的 40%, =取整 round(4/a)
             * （4）难度系数<=0.05 的题目，统一按照难度系数 0.05 处理。也就是说做对一道题最多加分 200 封顶
             */
            int score = 0;
            BigDecimal difficulty = exercise.getDifficulty();
            if (difficulty.floatValue() <= 0.05) {
                difficulty = new BigDecimal(new Double(0.05).toString());
            }
            switch (status) {
                case UserExerciseLog.STATUS_RIGHT_FIRST_TIME:
                    score = new BigDecimal(10).divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue();
                    break;
                case UserExerciseLog.STATUS_RIGHT_SECOND_TIME:
                    score = Math.round(new BigDecimal(6).divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue());
                    break;
                case UserExerciseLog.STATUS_GIVE_UP:
                case UserExerciseLog.STATUS_WRONG:
                    score = -Math.round(new BigDecimal(4).divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue());
                    break;
            }

            category.setScore(category.getScore() + score);
            userCategoryRepository.save(category);
        } else {
            //第二种情况：当一道题同时给两个小专题 A，B 加分减分时，
            // 第一小专题 A（即该道题目所 属于的小专题）加分减分量 = 第一种情况的 90%，
            // 第二小专题 B 加分减分量 = 第一种情况 的 25%
            ExerciseCategory secondCategory = exercise.getSecondCategory();
            UserCategory secondUserCategory = userCategoryRepository.findByUserAndCategory(student, secondCategory);
            if (null == secondUserCategory) {
                secondUserCategory = initUserCategory(student, secondCategory);
            }

            int score = 0;
            int secondScore = 0;
            BigDecimal difficulty = exercise.getDifficulty();
            if (difficulty.floatValue() <= 0.05) {
                difficulty = new BigDecimal(new Double(0.05).toString());
            }
            switch (status) {
                case UserExerciseLog.STATUS_RIGHT_FIRST_TIME:
                    score = new BigDecimal(9).divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue();
                    secondScore = new BigDecimal(new Double(2.5).toString())
                            .divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue();
                    break;
                case UserExerciseLog.STATUS_RIGHT_SECOND_TIME:
                    score = Math.round(new BigDecimal(new Double(5.4).toString())
                            .divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue());
                    secondScore = new BigDecimal(new Double(1.5).toString())
                            .divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue();
                    break;
                case UserExerciseLog.STATUS_GIVE_UP:
                case UserExerciseLog.STATUS_WRONG:
                    score = -Math.round(new BigDecimal(new Double(3.6).toString())
                            .divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue());
                    secondScore = -Math
                            .round(new BigDecimal(1).divide(difficulty, 0, RoundingMode.HALF_EVEN).intValue());
                    break;
            }

            category.setScore(category.getScore() + score);
            category = userCategoryRepository.save(category);

            secondUserCategory.setScore(secondUserCategory.getScore() + secondScore);
            userCategoryRepository.save(secondUserCategory);
        }

        //计算专题等级
        if (category.getScore() == 0) {
            //当某次做题做错减分使小专题积分归 0 时，触发 系统进入下一个小专题做题
            nextStudentExerciseCategory(student);
        }

        if (category.getScore() < 60 && category.getUserLevel() > 0) {
            //降级到user level 0
            category.setUserLevel(0);
        } else if (category.getScore() < 100 && category.getUserLevel() > 1) {
            //降级到user level 1
            category.setUserLevel(1);
        } else if (category.getScore() < 150 && category.getUserLevel() > 2) {
            //降级到user level 2
            category.setUserLevel(2);
        }


        if (category.getScore() >= 60 && category.getUserLevel() < 1) {
            //通过
            category.setUserLevel(1);
            userActivityService
                    .addActivity(student, String.format("恭喜您通过了专题%s", category.getCategory().getName()));

            nextStudentExerciseCategory(student);
        }
        if (category.getScore() >= 100 && category.getUserLevel() < 2) {
            //精通
            category.setUserLevel(2);
            userActivityService
                    .addActivity(student, String.format("恭喜您掌握了专题%s", category.getCategory().getName()));

            nextStudentExerciseCategory(student);
        }
        if (category.getScore() >= 150 && category.getUserLevel() < 3) {
            //优秀
            category.setUserLevel(3);
            userActivityService
                    .addActivity(student, String.format("恭喜您熟练掌握了专题%s", category.getCategory().getName()));

            nextStudentExerciseCategory(student);
        }
        if (category.getScore() >= Constants.MAX_USER_CATEGORY_SCORE && category.getUserLevel() < 4) {
            //满分
            category.setUserLevel(4);
            userActivityService
                    .addActivity(student, String.format("恭喜您精通了专题%s", category.getCategory().getName()));

            nextStudentExerciseCategory(student);
        }

        category = userCategoryRepository.save(category);

        //计算父分类的分数
        UserCategory parent = calculateParentUserCategoryScore(student, category);
        calculateParentUserCategoryScore(student, parent);
    }

    private UserCategory calculateParentUserCategoryScore(User student, UserCategory childUserCategory) {
        if (null == childUserCategory) {
            return null;
        }

        ExerciseCategory parent = childUserCategory.getCategory().getParent();
        if (null == parent) {
            return null;
        }
        if (parent.getLevel() > 1) {
            throw new BizException("只有父专题才能计算平均分数");
        }


        UserCategory existed = userCategoryRepository.findByUserAndCategory(student, parent);
        if (null == existed) {
            existed = initUserCategory(student, parent);
        }

        Double score = userCategoryRepository.avgScore(parent, student);
        existed.setScore(score.intValue());
        existed.setDifficultyLevel(userCategoryRepository.avgDifficultyLevel(parent, student).intValue());
        existed.setUserLevel(userCategoryRepository.avgUserLevel(parent, student).intValue());
        existed.setCounterOfWrong(userCategoryRepository.sumWrong(student, parent));
        existed.setCounterOfSecondRight(userCategoryRepository.sumSecondRight(student, parent));
        existed.setCounterOfFirstRight(userCategoryRepository.sumFirstRight(student, parent));
        existed.setCounterOfGiveup(userCategoryRepository.sumGiveup(student, parent));

        existed = userCategoryRepository.save(existed);
        return existed;
    }

    /**
     * 切换到下一个专题
     *
     * @param student
     */
    private void nextStudentExerciseCategory(User student) {
        UserCategory currentUserCategory = getStudentCurrentCategory(student);
        ExerciseCategory thirdCategory = currentUserCategory.getCategory();

        ExerciseCategory secondCategory = thirdCategory.getParent();
        //先找相同二级专题下其他未通过的三级专题
        UserCategory newThirdUserCategory = userCategoryRepository
                .findTop1ByUserAndCategoryParentAndScoreLessThanAndIdNot(student, secondCategory, 60,
                        currentUserCategory.getId());

        ExerciseCategory newThirdCategory = null;
        if (null != newThirdUserCategory && !currentUserCategory.getId().equals(newThirdUserCategory.getId())) {
            newThirdCategory = newThirdUserCategory.getCategory();
        }

        if (newThirdCategory == null) {
            //找相同二级专题下其他初始化的的三级专题
            Pageable pageable = PageRequest.of(0, 1);
            List<ExerciseCategory> queryResult = exerciseCategoryRepository
                    .findOtherThirdCategory(secondCategory, student, pageable);
            if (!queryResult.isEmpty()) {
                newThirdCategory = queryResult.get(0);
            }
        }

        if (null == newThirdCategory) {
            //找其他二级专题
            ExerciseCategory firstCategory = secondCategory.getParent();

            //先找相同一级专题下其他未通过的二级专题
            UserCategory newSecondUserCategory = userCategoryRepository
                    .findTop1ByUserAndCategoryParentAndScoreLessThan(student, firstCategory, 60);

            secondCategory = newSecondUserCategory.getCategory();
            newThirdUserCategory = userCategoryRepository
                    .findTop1ByUserAndCategoryParentAndScoreLessThanAndIdNot(student, secondCategory, 60,
                            currentUserCategory.getId());

            if (null != newThirdUserCategory && !currentUserCategory.getId().equals(newThirdUserCategory.getId())) {
                newThirdCategory = newThirdUserCategory.getCategory();
            }

            if (newThirdCategory == null) {
                //找相同二级专题下其他初始化的的三级专题
                Pageable pageable = PageRequest.of(0, 1);
                List<ExerciseCategory> queryResult = exerciseCategoryRepository
                        .findOtherThirdCategory(secondCategory, student, pageable);
                if (!queryResult.isEmpty()) {
                    newThirdCategory = queryResult.get(0);
                }
            }

//            throw new BizException("该二级专题下已经没有更多三级专题了，请您另外选择");
        }

        if (null == newThirdCategory) {
            cleanStudentCurrentCategory(student);
        } else {
            StudentSetCurrentCategoryParameter studentSetCurrentCategoryParameter = new StudentSetCurrentCategoryParameter();
            studentSetCurrentCategoryParameter.setCategoryId(newThirdCategory.getId());
            setStudentCurrentCategory(studentSetCurrentCategoryParameter, student);
        }
    }

    @Override
    public ExerciseGiveUpResponse giveUpExercise(@NotNull User student,
                                                 @NotNull @Valid ExerciseGiveUpParameter parameter) {
        Exercise exercise = findExerciseById(parameter.getExerciseId());
        UserExerciseLog log = userExerciseLogRepository.findByUserAndExercise(student, exercise);
        log.setAnswer(parameter.getAnswer());
        log.setStatus(UserExerciseLog.STATUS_GIVE_UP);
        userExerciseLogRepository.save(log);
        increaseUserExerciseLogStat(log);

        calculateUserScore(exercise, log.getStatus(), student);


        userActivityService.addActivity(student, String.format("放弃了题目: %s", exercise.getName()));

        ExerciseGiveUpResponse result = new ExerciseGiveUpResponse();
        result.setResult("您放弃了当前习题");
        return result;
    }

    @Override
    public ExerciseCategoryScoreDefinition createExerciseCategoryScoreDefinition(@NotNull Integer id,
                                                                                 @NotNull @Valid ExerciseCategoryScoreDefinitionCreateParameter parameter,
                                                                                 @NotNull User requireUser) {
        Exercise exercise = findExerciseById(id, requireUser);
        ExerciseCategory category = findExerciseCategoryById(parameter.getCategoryId(), requireUser);
        ExerciseCategoryScoreDefinition existed = exerciseCategoryScoreDefinitionRepository
                .findByCategoryAndExerciseAndStatus(category, exercise, parameter.getStatus());
        if (existed != null) {
            throw new BizException("该积分配置已经存在，请直接修改");
        }

        ExerciseCategoryScoreDefinition newOne = new ExerciseCategoryScoreDefinition();
        newOne.setCategory(category);
        newOne.setExercise(exercise);
        newOne.setScore(parameter.getScore());
        newOne.setStatus(parameter.getStatus());
        return exerciseCategoryScoreDefinitionRepository.save(newOne);
    }

    @Override
    public ExerciseCategoryScoreDefinition updateExerciseCategoryScoreDefinition(@NotNull Integer id,
                                                                                 @NotNull @Valid ExerciseCategoryScoreDefinitionUpdateParameter parameter,
                                                                                 @NotNull User requireUser) {
        ExerciseCategoryScoreDefinition existed = exerciseCategoryScoreDefinitionRepository.findById(id).orElse(null);
        if (null == existed) {
            throw new ResourceNotFoundException();
        }
        existed.setScore(parameter.getScore());
        return exerciseCategoryScoreDefinitionRepository.save(existed);
    }

    @Override
    public void deleteExerciseCategoryScoreDefinition(@NotNull Integer id, @NotNull User requireUser) {
        ExerciseCategoryScoreDefinition existed = exerciseCategoryScoreDefinitionRepository.findById(id).orElse(null);
        if (null == existed) {
            throw new ResourceNotFoundException();
        }
        exerciseCategoryScoreDefinitionRepository.delete(existed);
    }

    @Override
    public List<ExerciseCategoryScoreDefinition> listScoreDefinitions(@NotNull Integer id, @NotNull User requireUser) {
        Exercise exercise = findExerciseById(id, requireUser);
        return exerciseCategoryScoreDefinitionRepository.findByExercise(exercise);
    }

    @Override
    public Page<ExerciseCategory> queryExerciseCategories(@NotNull ExerciseCategoryQueryParameter2 parameter,
                                                          @NotNull User requireUser) {
        PageRequest pageable = PageRequest.of(parameter.getStart(), parameter.getLimit(), parameter.genSort());
        return exerciseCategoryRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.equal(root.get("deleted"), false));

                if (!StringUtils.isEmpty(parameter.getNameLike())) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%" + parameter.getNameLike() + "%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, pageable);
    }

    @Override
    public List<UserScore> listUserScores(@NotNull UserScoreListParameter parameter, @NotNull User user) {
        return userScoreRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                if (null != parameter.getUserId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("user").get("id"), parameter.getUserId()));
                }
                if (null != parameter.getCategoryId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("category").get("id"), parameter.getCategoryId()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, Sort.by(Sort.Direction.DESC, "score"));
    }

    @Override
    public ExerciseCategory findExerciseCategoryByName(@NotEmpty String name) {
        return exerciseCategoryRepository.findByName(name);
    }

    @Override
    public ExerciseCategory createExerciseCategoryIfNotExist(@NotEmpty String name, ExerciseCategory parent,
                                                             @NotNull User currentUser) {
        ExerciseCategory existed = findExerciseCategoryByName(name);
        if (null == existed) {
            ExerciseCategoryCreateParameter categoryCreateParameter = new ExerciseCategoryCreateParameter();
            categoryCreateParameter.setName(name);
            if (null != parent) {
                categoryCreateParameter.setParentId(parent.getId());
            }
            existed = createExerciseCategory(categoryCreateParameter, currentUser);
        }
        return existed;
    }

    @Override
    public List<UserCategory> listUserCategories(UserCategoryQueryParameter parameter) {
        return userCategoryRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                if (null != parameter.getUserId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("user").get("id"), parameter.getUserId()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    @Override
    public StudentProfile getStudentProfile(@NotNull User student) {
        StudentProfile result = new StudentProfile();
        UserCategorySummary rootUserCategory = new UserCategorySummary();
        result.setRootUserCategory(rootUserCategory);
        UserCategory currentUserCategory = getStudentCurrentCategory(student);
        ExerciseCategory firstCategory = currentUserCategory.getCategory().getParent().getParent();
        UserCategory firstUserCategory = userCategoryRepository.findByUserAndCategory(student, firstCategory);

        result.setRootUserCategory(UserCategorySummary.from(firstUserCategory));

        Pageable queryUserCategory = PageRequest.of(0, 99, Sort.by(Sort.Direction.ASC, "category.createdAt"));
        Page<UserCategory> queryUserCategoryResult = userCategoryRepository
                .findByUserAndCategoryLevel(student, 1, queryUserCategory);

        Set<Integer> hasScoreCategories = Sets.newHashSet();
        List<UserCategorySummary> categories = Lists.newArrayList();
        result.setTopUserCategories(categories);
        queryUserCategoryResult.getContent().forEach(userCategory -> {
            hasScoreCategories.add(userCategory.getCategory().getId());
            categories.add(UserCategorySummary.from(userCategory));
        });

        List<ExerciseCategory> secondCategories = exerciseCategoryRepository.findByParent(firstCategory);
        int counterOfNoScoreCategories = 0;
        for (ExerciseCategory category : secondCategories) {
            if (!hasScoreCategories.contains(category.getId())) {
                UserCategorySummary summary = new UserCategorySummary();
                summary.setUserLevel(0);
                summary.setDifficultyLevel(1);
                summary.setScore(10);
                summary.setCounterOfFirstRight(0);
                summary.setCounterOfGiveup(0);
                summary.setCounterOfSecondRight(0);
                summary.setCounterOfWrong(0);
                summary.setCategory(ExerciseCategorySummary.from(category));
                categories.add(summary);
                counterOfNoScoreCategories++;
            }
        }


        Map<Integer, Integer> thirdCategoryStats = Maps.newHashMap();
        result.setThirdCategoryStats(thirdCategoryStats);
        thirdCategoryStats.put(0, counterOfNoScoreCategories + userCategoryRepository
                .countByUserAndCategoryLevelAndUserLevel(student, 2, 0));
        thirdCategoryStats.put(1, userCategoryRepository.countByUserAndCategoryLevelAndUserLevel(student, 2, 1));
        thirdCategoryStats.put(2, userCategoryRepository.countByUserAndCategoryLevelAndUserLevel(student, 2, 2));
        thirdCategoryStats.put(3, userCategoryRepository
                .countByUserAndCategoryLevelAndUserLevel(student, 2, 3) + userCategoryRepository
                .countByUserAndCategoryLevelAndUserLevel(student, 2, 4));

        int year = Calendar.getInstance().get(Calendar.YEAR);
        Map<String, Integer> logStatMap = Maps.newHashMap();
        result.setExerciseLogStats(logStatMap);
        List<UserExerciseLogStat> logStats = userExerciseLogStatRepository.findByUserAndYear(student, year);
        for (UserExerciseLogStat logStat : logStats) {
            logStatMap.put(logStat.getYear() + "_" + logStat.getMonth() + "_" + logStat.getDay(), logStat.getCounter());
        }

        return result;
    }

    @Override
    public void calculateUserExerciseLogStats(@NotNull Integer id, @NotNull User currentUser) {
        User user = userService.findById(id);
        log.info(String.format("calculateUserExerciseLogStats for %s(%d)", user.getUsername(), user.getId()));
        userExerciseLogStatRepository.deleteByUser(user);

        int currentPage = 0;
        Pageable pageRequest = PageRequest.of(currentPage, 100);
        Page<UserExerciseLog> logs = userExerciseLogRepository.findByUser(user, pageRequest);
        while (!logs.isEmpty()) {
            for (UserExerciseLog log : logs) {
                increaseUserExerciseLogStat(log);
            }

            currentPage++;
            pageRequest = PageRequest.of(currentPage, 100);
            logs = userExerciseLogRepository.findByUser(user, pageRequest);
        }
    }

    @Override
    public StudentReport1 getStudentReport1(@NotNull Integer categoryId, @NotNull User student) {
        ExerciseCategory category = findExerciseCategoryById(categoryId);
        if (category.getLevel() != 2) {
            throw new BizException("该专题不是3级专题");
        }

        StudentReport1 result = new StudentReport1();
        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(student, category);
        if (userCategory == null) {
            userCategory = initUserCategory(student, category);
        }
        result.setThirdUserCategory(UserCategorySummary.from(userCategory));

        UserCategory secondUserCategory = userCategoryRepository.findByUserAndCategory(student, category.getParent());
        if (null == secondUserCategory) {
            secondUserCategory = initUserCategory(student, category.getParent());
        }
        result.setSecondUserCategory(UserCategorySummary.from(secondUserCategory));

        List<UserCategory> thirdUserCategories = userCategoryRepository
                .findByUserAndCategoryParent(student, category.getParent(),
                        Sort.by(Sort.Direction.ASC, "category.createdAt"));
        List<ExerciseCategory> thirdCategories = exerciseCategoryRepository.findByParent(category.getParent());
        Set<Integer> hasScoreCategories = Sets.newHashSet();
        List<UserCategorySummary> thirdCategorySummaries = Lists.newArrayList();
        result.setThirdUserCategories(thirdCategorySummaries);
        for (UserCategory item : thirdUserCategories) {
            thirdCategorySummaries.add(UserCategorySummary.from(item));
            hasScoreCategories.add(item.getCategory().getId());
        }

        for (ExerciseCategory item : thirdCategories) {
            if (!hasScoreCategories.contains(item.getId())) {
                thirdCategorySummaries.add(UserCategorySummary.from(initUserCategory(student, item)));
            }
        }

        List<UserExerciseLog> exerciseLogs = userExerciseLogRepository
                .findByUserAndCategoryAndStatusIn(student, category,
                        new int[]{UserExerciseLog.STATUS_RIGHT_FIRST_TIME, UserExerciseLog.STATUS_RIGHT_SECOND_TIME, UserExerciseLog.STATUS_GIVE_UP,
                                UserExerciseLog.STATUS_WRONG}, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        result.setExerciseLogs(exerciseLogs.stream().map(UserExerciseLogSummary::from).collect(Collectors.toList()));


        return result;
    }

    @Override
    public StudentReport2 getStudentReport2(@NotNull Integer categoryId, @NotNull User student) {
        ExerciseCategory category = findExerciseCategoryById(categoryId);
        if (category.getLevel() != 1) {
            throw new BizException("该专题不是2级专题");
        }

        StudentReport2 result = new StudentReport2();
        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(student, category);
        if (userCategory == null) {
            userCategory = initUserCategory(student, category);
        }
        result.setSecondUserCategory(UserCategorySummary.from(userCategory));

        List<UserCategory> thirdUserCategories = userCategoryRepository
                .findByUserAndCategoryParent(student, category, Sort.by(Sort.Direction.ASC, "category.createdAt"));
        List<ExerciseCategory> thirdCategories = exerciseCategoryRepository.findByParent(category);
        Set<Integer> hasScoreCategories = Sets.newHashSet();
        List<UserCategorySummary> thirdCategorySummaries = Lists.newArrayList();
        result.setThirdUserCategories(thirdCategorySummaries);
        for (UserCategory item : thirdUserCategories) {
            thirdCategorySummaries.add(UserCategorySummary.from(item));
            hasScoreCategories.add(item.getCategory().getId());
        }

        for (ExerciseCategory item : thirdCategories) {
            if (!hasScoreCategories.contains(item.getId())) {
                thirdCategorySummaries.add(UserCategorySummary.from(initUserCategory(student, item)));
            }
        }

        List<UserExerciseLog> exerciseLogs = userExerciseLogRepository
                .findByUserAndCategoryCodeLikeAndStatusIn(student, category.getCode() + "%",
                        new int[]{UserExerciseLog.STATUS_RIGHT_FIRST_TIME, UserExerciseLog.STATUS_RIGHT_SECOND_TIME, UserExerciseLog.STATUS_GIVE_UP,
                                UserExerciseLog.STATUS_WRONG}, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        result.setExerciseLogs(exerciseLogs.stream().map(UserExerciseLogSummary::from).collect(Collectors.toList()));
        return result;
    }

    @Override
    public StudentReport3 getMyStudentReport3(@NotNull Integer categoryId, @NotNull User student) {
        ExerciseCategory category = findExerciseCategoryById(categoryId);
        if (category.getLevel() != 0) {
            throw new BizException("该专题不是1级专题");
        }
        StudentReport3 result = new StudentReport3();

        UserCategory userCategory = userCategoryRepository.findByUserAndCategory(student, category);
        if (userCategory == null) {
            userCategory = initUserCategory(student, category);
        }
        result.setFirstUserCategory(UserCategorySummary.from(userCategory));

        List<UserCategory> secondUserCategories = userCategoryRepository
                .findByUserAndCategoryParent(student, category, Sort.by(Sort.Direction.ASC, "category.createdAt"));
        List<ExerciseCategory> secondCategories = exerciseCategoryRepository.findByParent(category);
        Set<Integer> hasScoreCategories = Sets.newHashSet();
        List<UserCategorySummary> secondCategorySummaries = Lists.newArrayList();
        result.setSecondUserCategories(secondCategorySummaries);
        for (UserCategory item : secondUserCategories) {
            secondCategorySummaries.add(UserCategorySummary.from(item));
            hasScoreCategories.add(item.getCategory().getId());
        }

        for (ExerciseCategory item : secondCategories) {
            if (!hasScoreCategories.contains(item.getId())) {
                secondCategorySummaries.add(UserCategorySummary.from(initUserCategory(student, item)));
            }
        }

        Map<String, Integer> thirdCategoryStats = Maps.newHashMap();
        result.setThirdCategoryStats(thirdCategoryStats);
        thirdCategoryStats.put("未通过", userCategoryRepository
                .countByUserAndCategoryCodeLikeAndCategoryLevelAndUserLevel(student, category.getCode() + "%", 2, 0));
        thirdCategoryStats.put("通过",
                userCategoryRepository
                        .countByUserAndCategoryCodeLikeAndCategoryLevelAndUserLevel(student, category.getCode() + "%",
                                2, 1));
        thirdCategoryStats.put("掌握",
                userCategoryRepository
                        .countByUserAndCategoryCodeLikeAndCategoryLevelAndUserLevel(student, category.getCode() + "%",
                                2, 2));
        thirdCategoryStats.put("熟练掌握", userCategoryRepository
                .countByUserAndCategoryCodeLikeAndCategoryLevelAndUserLevel(student, category.getCode() + "%", 2,
                        3) + userCategoryRepository
                .countByUserAndCategoryCodeLikeAndCategoryLevelAndUserLevel(student, category.getCode() + "%", 2, 4));

        Map<String, Integer> exerciseLogStats = Maps.newConcurrentMap();
        result.setExerciseLogStats(exerciseLogStats);
        exerciseLogStats.put("一对",
                userExerciseLogRepository.countByUserAndCategoryCodeLikeAndStatus(student, category.getCode() + "%",
                        UserExerciseLog.STATUS_RIGHT_FIRST_TIME));
        exerciseLogStats.put("二对",
                userExerciseLogRepository.countByUserAndCategoryCodeLikeAndStatus(student, category.getCode() + "%",
                        UserExerciseLog.STATUS_RIGHT_SECOND_TIME));
        exerciseLogStats.put("错误",
                userExerciseLogRepository.countByUserAndCategoryCodeLikeAndStatus(student, category.getCode() + "%",
                        UserExerciseLog.STATUS_WRONG));
        exerciseLogStats.put("放弃",
                userExerciseLogRepository.countByUserAndCategoryCodeLikeAndStatus(student, category.getCode() + "%",
                        UserExerciseLog.STATUS_GIVE_UP));

        return result;
    }

    @Override
    public UserExerciseLog getUserExerciseLogById(@NotNull Integer id, @NotNull User currentUser) {
        return userExerciseLogRepository.findById(id).orElse(null);
    }

    private UserCategory initUserCategory(User student, ExerciseCategory category) {
        UserCategory userCategory = new UserCategory();
        userCategory.setUser(student);
        userCategory.setCategory(category);
        userCategory.setCategoryCode(category.getCode());
        userCategory.setScore(START_CATEGORY_SCORE);
        userCategory.setCounterOfGiveup(0);
        userCategory.setCounterOfFirstRight(0);
        userCategory.setCounterOfSecondRight(0);
        userCategory.setCounterOfWrong(0);
        userCategory.setUserLevel(0);
        userCategory.setDifficultyLevel(1);
        return userCategoryRepository.save(userCategory);

    }

    private Exercise findExerciseById(Integer exerciseId) {
        Exercise existed = exerciseRepository.findById(exerciseId).orElse(null);
        if (null == existed) {
            throw new ResourceNotFoundException();
        }
        if (existed.isDeleted()) {
            throw new ResourceNotFoundException();
        }
        return existed;
    }

    private ExerciseCategory findExerciseCategoryById(Integer categoryId) {
        ExerciseCategory existed = exerciseCategoryRepository.findById(categoryId).orElse(null);
        if (null == existed) {
            throw new ResourceNotFoundException();
        }
        if (existed.isDeleted()) {
            throw new ResourceNotFoundException();
        }
        return existed;
    }

    public Exercise findExerciseById(Integer id, User requireUser) {
        Exercise existed = exerciseRepository.findById(id).orElse(null);
        if (null == existed) {
            throw new ResourceNotFoundException();
        }
        if (existed.isDeleted()) {
            throw new ResourceNotFoundException();
        }
        return existed;
    }

    public ExerciseTag createTagIfNotExisted(String name) {
        ExerciseTag existed = exerciseTagRepository.findByName(name);
        if (null == existed) {
            existed = new ExerciseTag();
            existed = exerciseTagRepository.save(existed);
        }

        return existed;
    }
}
