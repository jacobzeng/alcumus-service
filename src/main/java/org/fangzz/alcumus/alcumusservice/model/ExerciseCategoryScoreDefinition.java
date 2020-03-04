package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_exercise_category_score_definitions")
public class ExerciseCategoryScoreDefinition extends BaseEntity {
    @ManyToOne
    private Exercise exercise;
    @ManyToOne(fetch = FetchType.EAGER)
    private ExerciseCategory category;
    private int status; //等于UserExerciseLog中的status
    private int score;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
