package org.fangzz.alcumus.alcumusservice.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fangzz.alcumus.alcumusservice.dto.param.StudentRegisterParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserCreateParameter;
import org.fangzz.alcumus.alcumusservice.dto.param.UserQueryParameter;
import org.fangzz.alcumus.alcumusservice.exception.BizException;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserRole;
import org.fangzz.alcumus.alcumusservice.repository.UserRepository;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Validated
@Service
public class UserServiceImpl implements UserService {
    private final static Log log = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(@NotEmpty String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User createUser(@NotNull @Valid UserCreateParameter parameter) {
        log.info(String.format("create user %s", parameter.getUsername()));
        User user = new User();
        user.setUsername(parameter.getUsername());
        user.setNickname(parameter.getNickname());
        user.setPassword(bCryptPasswordEncoder.encode(parameter.getPassword()));
        if (parameter.getRoles().length == 0) {
            user.setRoles(Arrays.asList(UserRole.ROLE_USER));
        } else {
            user.setRoles(Arrays.asList(parameter.getRoles()));
        }
        return userRepository.save(user);
    }

    @Override
    public User createStudentAccount(@NotNull @Valid StudentRegisterParameter parameter) {
        if (!parameter.getPassword().equals(parameter.getPasswordAgain())) {
            throw new BizException("两次输入的密码不一致");
        }
        UserCreateParameter userCreateParameter = new UserCreateParameter();
        userCreateParameter.setNickname(parameter.getNickname());
        userCreateParameter.setUsername(parameter.getUsername());
        userCreateParameter.setPassword(parameter.getPassword());
        userCreateParameter.setRoles(new UserRole[]{UserRole.ROLE_USER, UserRole.ROLE_STUDENT});
        return createUser(userCreateParameter);
    }

    @Override
    public User findById(@NotNull Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Page<User> query(@NotNull UserQueryParameter parameter) {
        Pageable pageable = PageRequest.of(parameter.getStart(), parameter.getLimit(), parameter.genSort());
        return userRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                if (!Strings.isNullOrEmpty(parameter.getUsernameLike())) {
                    predicateList
                            .add(criteriaBuilder.like(root.get("username"), "%" + parameter.getUsernameLike() + "%"));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, pageable);
    }
}
