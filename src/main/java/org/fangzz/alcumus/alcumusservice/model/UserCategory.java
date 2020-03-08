package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_categories")
public class UserCategory extends BaseEntity {
    @ManyToOne
    private User user;
    @ManyToOne
    private ExerciseCategory category;

    private boolean current;

    private int counterOfFirstRight = 0;
    private int counterOfWrong = 0;
    private int counterOfSecondRight = 0;
    private int counterOfGiveup = 0;
    private int score = 0;
    private int difficultyLevel = 1;

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        if (difficultyLevel > 15) {
            difficultyLevel = 15;
        } else if (difficultyLevel < 1) {
            difficultyLevel = 1;
        }
        this.difficultyLevel = difficultyLevel;
    }

    public int getCounterOfFirstRight() {
        return counterOfFirstRight;
    }

    public void setCounterOfFirstRight(int counterOfFirstRight) {
        this.counterOfFirstRight = counterOfFirstRight;
    }

    public int getCounterOfWrong() {
        return counterOfWrong;
    }

    public void setCounterOfWrong(int counterOfWrong) {
        this.counterOfWrong = counterOfWrong;
    }

    public int getCounterOfSecondRight() {
        return counterOfSecondRight;
    }

    public void setCounterOfSecondRight(int counterOfSecondRight) {
        this.counterOfSecondRight = counterOfSecondRight;
    }

    public int getCounterOfGiveup() {
        return counterOfGiveup;
    }

    public void setCounterOfGiveup(int counterOfGiveup) {
        this.counterOfGiveup = counterOfGiveup;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
