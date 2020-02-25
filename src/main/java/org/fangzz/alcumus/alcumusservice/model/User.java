package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户
 */

@Entity
@Table(name = "t_users")
public class User extends DeletedAbleEntity {
    @Column(length = 50, unique = true)
    private String username;
    @Column(length = 50)
    private String nickname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    @Column(length = 100)
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<UserRole> roles;

    public String[] getRolesArrayInString() {
        if (null == getRoles()) {
            return new String[]{};
        }
        return getRoles().stream().map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[]{});
    }
}
