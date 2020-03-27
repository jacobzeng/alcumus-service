package org.fangzz.alcumus.alcumusservice.web.my;

import org.fangzz.alcumus.alcumusservice.dto.*;
import org.fangzz.alcumus.alcumusservice.dto.param.UserActivityQueryParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserActivity;
import org.fangzz.alcumus.alcumusservice.model.UserCategory;
import org.fangzz.alcumus.alcumusservice.service.ExerciseService;
import org.fangzz.alcumus.alcumusservice.service.UserActivityService;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping("/api")
public class MyRestController extends UserAwareController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/my")
    @Transactional(readOnly = true)
    public UserDetail my() {
        User user = requireUser();
        return UserDetail.from(user);
    }

    @GetMapping("/my/activities")
    @Transactional(readOnly = true)
    public Page<UserActivitySummary> myActivities(UserActivityQueryParameter parameter) {
        User user = requireUser();
        parameter.setUserId(user.getId());
        Page<UserActivity> queryResult = userActivityService.query(parameter, user);

        return new PageImpl<>(
                queryResult.getContent().stream().map(UserActivitySummary::from).collect(Collectors.toList()),
                queryResult.getPageable(),
                queryResult.getTotalElements()
        );
    }

    @GetMapping("/my/current-user-category")
    @Transactional(readOnly = true)
    public UserCategorySummary getMyCurrentUserCategory() {
        UserCategory queryResult = exerciseService.getStudentCurrentCategory(currentUser());
        return UserCategorySummary.from(queryResult);
    }

    @GetMapping("/my/student-profile")
    @Transactional(readOnly = true)
    public StudentProfile getMyStudentProfile() {
        return exerciseService.getStudentProfile(currentUser());
    }

    @GetMapping("/my/report1/{categoryId}")
    @Transactional(readOnly = true)
    public StudentReport1 getMyStudentReport1(@PathVariable Integer categoryId) {
        return exerciseService.getStudentReport1(categoryId, currentUser());
    }

    @GetMapping("/my/report2/{categoryId}")
    @Transactional(readOnly = true)
    public StudentReport2 getMyStudentReport2(@PathVariable Integer categoryId) {
        return exerciseService.getStudentReport2(categoryId, currentUser());
    }

    @GetMapping("/my/report3/{categoryId}")
    @Transactional(readOnly = true)
    public StudentReport3 getMyStudentReport3(@PathVariable Integer categoryId) {
        return exerciseService.getMyStudentReport3(categoryId, currentUser());
    }
}
