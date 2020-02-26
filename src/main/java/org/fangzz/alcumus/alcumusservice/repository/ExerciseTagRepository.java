package org.fangzz.alcumus.alcumusservice.repository;

import org.fangzz.alcumus.alcumusservice.model.ExerciseTag;

public interface ExerciseTagRepository extends AbstractRepository<ExerciseTag> {
    ExerciseTag findByName(String name);
}
