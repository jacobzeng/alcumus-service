package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_scores")
public class UserScore extends BaseEntity {
    @ManyToOne
    private User user;
    private int score = 0;
    @ManyToOne(fetch = FetchType.EAGER)
    private ExerciseCategory category;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }
}

