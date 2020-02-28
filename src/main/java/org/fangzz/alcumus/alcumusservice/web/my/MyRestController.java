package org.fangzz.alcumus.alcumusservice.web.my;

import org.fangzz.alcumus.alcumusservice.dto.UserActivitySummary;
import org.fangzz.alcumus.alcumusservice.dto.UserDetail;
import org.fangzz.alcumus.alcumusservice.dto.param.UserActivityQueryParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserActivity;
import org.fangzz.alcumus.alcumusservice.service.UserActivityService;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
}
