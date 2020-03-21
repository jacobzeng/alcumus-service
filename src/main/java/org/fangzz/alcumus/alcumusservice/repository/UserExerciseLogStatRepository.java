package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.User;
import org.fangzz.alcumus.alcumusservice.model.UserExerciseLogStat;

import java.util.List;

public interface UserExerciseLogStatRepository extends AbstractRepository<UserExerciseLogStat> {
    UserExerciseLogStat findByUserAndYearAndMonthAndDay(User user, int year, int month, int day);

    void deleteByUser(User user);

    List<UserExerciseLogStat> findByUserAndYear(User student, int year);
}
