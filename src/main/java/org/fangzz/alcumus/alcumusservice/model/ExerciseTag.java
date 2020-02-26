package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_exercise_tags")
public class ExerciseTag extends BaseEntity {
    @Column(length = 10, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
