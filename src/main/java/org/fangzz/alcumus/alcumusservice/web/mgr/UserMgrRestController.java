package org.fangzz.alcumus.alcumusservice.web.mgr;

import org.fangzz.alcumus.alcumusservice.dto.UserSummary;
import org.fangzz.alcumus.alcumusservice.dto.param.UserQueryParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
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

/**
 * 用户管理的rest controller
 */

@RestController
@RequestMapping("/api")
@Transactional
public class UserMgrRestController extends UserAwareController {

    @Autowired
    private UserService userService;

    @GetMapping("/mgr/users")
    public Page<UserSummary> queryUsers(UserQueryParameter parameter) {
        Page<User> queryResult = userService.query(parameter);
        return new PageImpl<>(
                queryResult.getContent().stream().map(UserSummary::from).collect(Collectors.toList()),
                queryResult.getPageable(),
                queryResult.getTotalElements()
        );
    }

}
