package org.fangzz.alcumus.alcumusservice.service.impl;

import com.google.common.collect.Lists;
import org.fangzz.alcumus.alcumusservice.dto.param.UserActivityQueryParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserActivity;
import org.fangzz.alcumus.alcumusservice.repository.UserActivityRepository;
import org.fangzz.alcumus.alcumusservice.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class UserActivityServiceImpl implements UserActivityService {
    @Autowired
    private UserActivityRepository userActivityRepository;

    @Override
    public UserActivity addActivity(@NotNull User student, @NotEmpty String content) {

        UserActivity activity = new UserActivity();
        activity.setUser(student);
        activity.setLog(content);

        return userActivityRepository.save(activity);
    }

    @Override
    public Page<UserActivity> query(@NotNull final UserActivityQueryParameter parameter, @NotNull User user) {
        Pageable pageRequest = PageRequest.of(parameter.getStart(), parameter.getLimit(), parameter.genSort());
        return userActivityRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = Lists.newArrayList();
                if (null != parameter.getUserId()) {
                    predicateList.add(criteriaBuilder.equal(root.get("user").get("id"), parameter.getUserId()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }
        }, pageRequest);
    }
}
