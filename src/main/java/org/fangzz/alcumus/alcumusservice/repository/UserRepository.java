package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.User;

public interface UserRepository extends AbstractRepository<User> {
    User findByUsername(String username);

    User findByUsernameAndDeletedIsFalse(String name);

    int countByUsername(String adminUsername);
}
