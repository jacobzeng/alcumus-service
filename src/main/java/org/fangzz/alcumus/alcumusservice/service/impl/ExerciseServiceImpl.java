package org.fangzz.alcumus.alcumusservice.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fangzz.alcumus.alcumusservice.dto.ExerciseAnswerResponse;
import org.fangzz.alcumus.alcumusservice.dto.ExerciseGiveUpResponse;
import org.fangzz.alcumus.alcumusservice.dto.param.*;
import org.fangzz.alcumus.alcumusservice.exception.BizException;
import org.fangzz.alcumus.alcumusservice.exception.ResourceNotFoundException;
import org.fangzz.alcumus.alcumusservice.model.*;
import org.fangzz.alcumus.alcumusservice.repository.*;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.service.UserActivityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
        }, Sort.by(Sort.Direction.ASC, "name"));
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
        }
        Exercise model = new Exercise();
        model.setCategory(category);
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
    public ExerciseCategory getStudentCurrentCategory(@NotNull User student) {
        UserCategory existed = userCategoryRepository.findByUserAndCurrent(student, true);
        if (existed != null) {
            return existed.getCategory();
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
        }, Sort.by(Sort.Direction.ASC, "name"));
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
        UserCategory existed = userCategoryRepository.findByUserAndCategory(currentUser, category);
        if (null == existed) {
            existed = new UserCategory();
            existed.setCategory(category);
            existed.setUser(currentUser);
            existed.setCounterOfDone(0);
            existed.setCounterOfRight(0);
            existed.setCounterOfWrong(0);

        }
        existed.setCurrent(true);
        existed = userCategoryRepository.save(existed);
        return existed;
    }

    @Override
    public Exercise nextStudentExercise(@NotNull User currentUser) {
        ExerciseCategory category = getStudentCurrentCategory(currentUser);
        if (null == category) {
            throw new BizException("请您先选择一个分类");
        }
        //1. 查找当前有没有正在做的题
        UserExerciseLog log = userExerciseLogRepository
                .findByUserAndCategoryAndStatus(currentUser, category, UserExerciseLog.STATUS_CURRENT);
        if (null != log) {
            return log.getExercise();
        }

        //2. 选择一道题目
        long total = exerciseRepository.countByCategoryAndDeleted(category, false);
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Exercise> exercises = exerciseRepository.nextStudentExercises(category, currentUser, pageRequest);
        if (exercises.isEmpty()) {
            throw new BizException("抱歉，该分类没有更多的练习题了");
        }
        Exercise result = exercises.get(0);

        UserExerciseLog newLog = new UserExerciseLog();
        newLog.setCategory(category);
        newLog.setExercise(result);
        newLog.setUser(currentUser);
        newLog.setStatus(UserExerciseLog.STATUS_CURRENT);
        userExerciseLogRepository.save(newLog);

        return result;
    }

    @Override
    public ExerciseAnswerResponse submitStudentAnswer(@NotNull User student,
                                                      @NotNull @Valid ExerciseAnswerParameter parameter) {
        Exercise exercise = findExerciseById(parameter.getExerciseId());
        UserExerciseLog log = userExerciseLogRepository.findByUserAndExercise(student, exercise);

        ExerciseAnswerResponse result = new ExerciseAnswerResponse();

        if (!exercise.getAnswer().equals(parameter.getAnswer())) {
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
        addUserScore(exercise, log.getStatus(), student);
        return result;
    }

    private void addUserScore(Exercise exercise, int status, User student) {
        List<ExerciseCategoryScoreDefinition> scoreDefinitions = exerciseCategoryScoreDefinitionRepository
                .findByExerciseAndStatus(exercise, status);
        for (ExerciseCategoryScoreDefinition scoreDefinition : scoreDefinitions) {
            ExerciseCategory category = scoreDefinition.getCategory();
            UserScore existed = userScoreRepository.findByUserAndCategory(student, category);
            if (null == existed) {
                existed = new UserScore();
                existed.setCategory(category);
                existed.setUser(student);
                existed.setScore(0);
            }

            existed.setScore(existed.getScore() + scoreDefinition.getScore());
            userScoreRepository.save(existed);
        }
    }

    @Override
    public ExerciseGiveUpResponse giveUpExercise(@NotNull User student,
                                                 @NotNull @Valid ExerciseGiveUpParameter parameter) {
        Exercise exercise = findExerciseById(parameter.getExerciseId());
        UserExerciseLog log = userExerciseLogRepository.findByUserAndExercise(student, exercise);
        log.setStatus(UserExerciseLog.STATUS_GIVE_UP);
        userExerciseLogRepository.save(log);

        addUserScore(exercise, log.getStatus(), student);

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
