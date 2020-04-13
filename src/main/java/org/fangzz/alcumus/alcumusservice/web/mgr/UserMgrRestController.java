package org.fangzz.alcumus.alcumusservice.web.mgr;

import org.fangzz.alcumus.alcumusservice.dto.UserSummary;
import org.fangzz.alcumus.alcumusservice.dto.param.UserCreateParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserQueryParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserUpdateParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.fangzz.alcumus.alcumusservice.web.UserAwareController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Transactional(readOnly = true)
    public Page<UserSummary> queryUsers(UserQueryParameter parameter) {
        Page<User> queryResult = userService.query(parameter);
        return new PageImpl<>(
                queryResult.getContent().stream().map(UserSummary::from).collect(Collectors.toList()),
                queryResult.getPageable(),
                queryResult.getTotalElements()
        );
    }

    @PostMapping("/mgr/users/{id}")
    public UserSummary updateUser(@PathVariable Integer id, @RequestBody UserUpdateParameter parameter) {
        User user = userService.updateUser(id, parameter, requireUser());
        return UserSummary.from(user);
    }

    @PostMapping("/mgr/users")
    public UserSummary createUser(@RequestBody UserCreateParameter parameter) {
        User user = userService.createUser(parameter, requireUser());
        return UserSummary.from(user);
    }

    @DeleteMapping("/mgr/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id, requireUser());
    }

}
