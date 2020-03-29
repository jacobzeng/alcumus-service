package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_exercise_logs")
public class UserExerciseLog extends BaseEntity {
    public final static int STATUS_CURRENT = 1;
    public final static int STATUS_RIGHT_FIRST_TIME = 2;
    public final static int STATUS_RIGHT_SECOND_TIME = 3;
    public final static int STATUS_GIVE_UP = 4;
    public final static int STATUS_WRONG = 5;
    @ManyToOne
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Exercise exercise;
    private int status = STATUS_CURRENT;
    @ManyToOne
    private ExerciseCategory category;

    private String categoryCode;

    private String answer;
    private String answer2;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
