package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;
import org.fangzz.alcumus.alcumusservice.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseCategoryRepository extends AbstractRepository<ExerciseCategory> {
    ExerciseCategory findByName(String name);

    @Query(value = "select a from ExerciseCategory a where a.parent=:secondCategory and a.id not in(select b.category.id from UserCategory b where b.user=:student)")
    List<ExerciseCategory> findOtherThirdCategory(@Param("secondCategory") ExerciseCategory secondCategory,
                                                  @Param("student") User student, Pageable pageable);
}
