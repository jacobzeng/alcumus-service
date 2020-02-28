package org.fangzz.alcumus.alcumusservice.web.guest;

import org.fangzz.alcumus.alcumusservice.dto.BaseDto;
import org.fangzz.alcumus.alcumusservice.dto.param.StudentRegisterParameter;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
@Transactional
public class RegisterRestController {
    @Autowired
    private UserService userService;

    @PostMapping("/register-student")
    public BaseDto registerStudentAccount(@RequestBody StudentRegisterParameter parameter) {
        User user = userService.createStudentAccount(parameter);
        BaseDto dto = new BaseDto();
        dto.setId(user.getId());
        return dto;
    }
}
