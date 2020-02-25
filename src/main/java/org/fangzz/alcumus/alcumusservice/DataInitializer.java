package org.fangzz.alcumus.alcumusservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fangzz.alcumus.alcumusservice.dto.param.UserCreateParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserRole;
import org.fangzz.alcumus.alcumusservice.repository.UserRepository;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final static Log log = LogFactory.getLog(DataInitializer.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        final String adminUsername = "admin";
        final String adminPassword = "123456";

        if (userRepository.countByUsername(adminUsername) == 0) {
            log.info("create default admin user");

            UserCreateParameter parameter = new UserCreateParameter();
            parameter.setUsername(adminUsername);
            parameter.setNickname(adminUsername);
            parameter.setPassword(adminPassword);
            parameter.setRoles(new UserRole[]{UserRole.ROLE_SYS_ADMIN, UserRole.ROLE_USER});
            User user = userService.createUser(parameter);
        }
    }
}
