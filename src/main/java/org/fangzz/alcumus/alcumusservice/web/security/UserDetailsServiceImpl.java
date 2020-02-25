package org.fangzz.alcumus.alcumusservice.web.security;

import com.google.common.collect.Lists;
import org.fangzz.alcumus.alcumusservice.model.UserRole;
import org.fangzz.alcumus.alcumusservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        org.fangzz.alcumus.alcumusservice.model.User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Collection<SimpleGrantedAuthority> roles = Lists.newArrayList();
        if (null != user.getRoles()) {
            for (UserRole role : user.getRoles()) {
                roles.add(new SimpleGrantedAuthority(role.name()));
            }
        }
        return new User(user.getUsername(), user.getPassword(), roles);
    }
}
