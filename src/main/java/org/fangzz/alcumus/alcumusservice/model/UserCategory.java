package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_categories")
public class UserCategory extends BaseEntity{
    @ManyToOne
    private User user;
    @ManyToOne
    private ExerciseCategory category;

    private boolean current;

    private int counterOfDone;
    private int counterOfRight;
    private int counterOfWrong;

    public int getCounterOfDone() {
        return counterOfDone;
    }

    public void setCounterOfDone(int counterOfDone) {
        this.counterOfDone = counterOfDone;
    }

    public int getCounterOfRight() {
        return counterOfRight;
    }

    public void setCounterOfRight(int counterOfRight) {
        this.counterOfRight = counterOfRight;
    }

    public int getCounterOfWrong() {
        return counterOfWrong;
    }

    public void setCounterOfWrong(int counterOfWrong) {
        this.counterOfWrong = counterOfWrong;
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
